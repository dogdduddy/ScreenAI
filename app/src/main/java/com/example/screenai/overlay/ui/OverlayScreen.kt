package com.example.screenai.overlay.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.screenai.domain.model.MenuAction
import com.example.screenai.overlay.viewmodel.OverlayViewModel

@Composable
fun OverlayScreen(
    viewModel: OverlayViewModel,
    onRequestScreenshot: () -> Bitmap?,
    onUpdateFocusable: (Boolean) -> Unit,
    onExpandOverlay: (Boolean) -> Unit,
    onDragButton: (Float, Float) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    var dropDownMenuSize by remember { mutableStateOf(IntSize(0, 0)) }

    // 메뉴나 입력창이 열릴 때 포커스 가능하게 설정
    val needsFocus = uiState.isPromptInputVisible ||
                     uiState.isResultVisible ||
                     uiState.isCustomPromptListVisible

    // 확장이 필요한 상태 (메뉴, 입력창, 결과창 등)
    val needsExpand = uiState.isMenuExpanded ||
                      uiState.isPromptInputVisible ||
                      uiState.isResultVisible ||
                      uiState.isCustomPromptListVisible

    // 포커스 상태 업데이트
    LaunchedEffect(needsFocus) {
        onUpdateFocusable(needsFocus)
    }

    // 오버레이 크기 업데이트
    LaunchedEffect(needsExpand) {
        onExpandOverlay(needsExpand)
    }

    // 버튼이 화면 하단 절반에 있으면 메뉴를 위로, 아니면 아래로
    val showMenuAbove = uiState.buttonY > screenHeightPx / 2

    Box(modifier = Modifier.fillMaxSize()) {

        // 플로팅 버튼 - 축소 상태에서는 왼쪽 상단에 위치
        FloatingButton(
            isExpanded = uiState.isMenuExpanded,
            onToggle = { viewModel.toggleMenu() },
            onDrag = { dx, dy -> if (!needsExpand) onDragButton(dx, dy) },
            modifier = Modifier
                .then(
                    if (needsExpand)
                        Modifier.offset { IntOffset(uiState.buttonX + 16.dp.roundToPx(), uiState.buttonY + 16.dp.roundToPx()) }
                    else
                        Modifier.align(Alignment.TopStart).padding(16.dp)
                )

        )

        // 드롭다운 메뉴
        DropdownMenu(
            isVisible = uiState.isMenuExpanded,
            onMenuAction = { action ->
                val screenshot = if (action != MenuAction.CUSTOM_PROMPT || uiState.customPrompts.isNotEmpty()) {
                    onRequestScreenshot()
                } else null
                viewModel.onMenuAction(action, screenshot)
            },
            onDismiss = { viewModel.closeMenu() },
            showAbove = showMenuAbove,
            modifier = Modifier
                .onGloballyPositioned {
                    dropDownMenuSize = it.size
                }
                .offset {
                    IntOffset(
                        uiState.buttonX + 16.dp.roundToPx(),
                        if (showMenuAbove) {
                            uiState.buttonY - dropDownMenuSize.height - 8.dp.roundToPx() // 버튼 위에 표시
                        } else {
                            uiState.buttonY + 72.dp.roundToPx() // 버튼 아래에 표시 (56dp 버튼 + 16dp 간격)
                        }
                    )
                }
        )

        // 커스텀 프롬프트 리스트
        CustomPromptListOverlay(
            isVisible = uiState.isCustomPromptListVisible,
            prompts = uiState.customPrompts,
            onSelectPrompt = { viewModel.selectCustomPrompt(it) },
            onDismiss = { viewModel.closeCustomPromptList() }
        )

        // 프롬프트 입력창
        PromptInputOverlay(
            isVisible = uiState.isPromptInputVisible,
            promptText = uiState.promptInput,
            isLoading = uiState.isLoading,
            onPromptChange = { viewModel.updatePromptInput(it) },
            onSubmit = { viewModel.submitPrompt() },
            onDismiss = { viewModel.closePromptInput() }
        )

        // 결과 표시
        ResultOverlay(
            isVisible = uiState.isResultVisible,
            isLoading = uiState.isLoading,
            result = uiState.result,
            onDismiss = { viewModel.closeResult() }
        )
    }
}
