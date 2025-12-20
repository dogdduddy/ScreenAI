package com.example.screenai.overlay.ui

import android.graphics.Bitmap
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.example.screenai.domain.model.MenuAction
import com.example.screenai.overlay.viewmodel.OverlayViewModel
import kotlin.math.roundToInt

@Composable
fun OverlayScreen(
    composeView: ComposeView,
    windowManager: WindowManager,
    layoutParams: WindowManager.LayoutParams,
    viewModel: OverlayViewModel,
    onRequestScreenshot: () -> Bitmap?,
    onUpdateFocusable: (Boolean) -> Unit,
    onExpandOverlay: (Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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

    Box(modifier = Modifier.fillMaxSize()) {
        // 플로팅 버튼 - 축소 상태에서는 왼쪽 상단에 위치
        FloatingButton(
            isExpanded = uiState.isMenuExpanded,
            onToggle = { viewModel.toggleMenu() },
            onDrag = { dx, dy ->
                // 축소 상태에서만 드래그로 위치 이동
                if (!needsExpand) {
                    layoutParams.x += dx.roundToInt()
                    layoutParams.y += dy.roundToInt()
                    windowManager.updateViewLayout(composeView, layoutParams)
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
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
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 80.dp, top = 16.dp)
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
