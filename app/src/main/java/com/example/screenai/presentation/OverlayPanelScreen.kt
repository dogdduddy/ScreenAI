package com.example.screenai.overlay.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.screenai.domain.model.MenuAction
import com.example.screenai.overlay.viewmodel.OverlayViewModel
import com.example.screenai.overlay.window.OverlayPanelComposeWindow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun OverlayPanelScreen(
    viewModel: OverlayViewModel,
    anchorFlow: StateFlow<OverlayPanelComposeWindow.Anchor>,
    onRequestScreenshot: () -> Bitmap?,
    onUpdateFocusable: (Boolean) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val anchor by anchorFlow.collectAsState()

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    var dropDownMenuSize by remember { mutableStateOf(IntSize(0, 0)) }

    // 메뉴나 입력창이 열릴 때 포커스 가능하게 설정
    val needsFocus = uiState.isPromptInputVisible ||
            uiState.isResultVisible ||
            uiState.isCustomPromptListVisible

    LaunchedEffect(needsFocus) {
        onUpdateFocusable(needsFocus)
    }

    // 버튼이 화면 하단 절반에 있으면 메뉴를 위로, 아니면 아래로
    val showMenuAbove = anchor.y > screenHeightPx / 2

    Box(modifier = Modifier.fillMaxSize()) {

        // 드롭다운 메뉴
        DropdownMenu(
            isVisible = uiState.isMenuExpanded,
            onMenuAction = { action ->
                val screenshot =
                    if (action != MenuAction.CUSTOM_PROMPT || uiState.customPrompts.isNotEmpty()) {
                        onRequestScreenshot()
                    } else null
                viewModel.onMenuAction(action, screenshot)
            },
            onDismiss = { viewModel.closeMenu() },
            showAbove = showMenuAbove,
            modifier = Modifier
                .onGloballyPositioned { dropDownMenuSize = it.size }
                .offset {
                    IntOffset(
                        anchor.x + 16.dp.roundToPx(),
                        if (showMenuAbove) {
                            anchor.y - dropDownMenuSize.height - 8.dp.roundToPx()
                        } else {
                            anchor.y + 72.dp.roundToPx()
                        }
                    )
                }
        )

        CustomPromptListOverlay(
            isVisible = uiState.isCustomPromptListVisible,
            prompts = uiState.customPrompts,
            onSelectPrompt = { viewModel.selectCustomPrompt(it) },
            onDismiss = { viewModel.closeCustomPromptList() }
        )

        PromptInputOverlay(
            isVisible = uiState.isPromptInputVisible,
            promptText = uiState.promptInput,
            isLoading = uiState.isLoading,
            onPromptChange = { viewModel.updatePromptInput(it) },
            onSubmit = { viewModel.submitPrompt() },
            onDismiss = { viewModel.closePromptInput() }
        )

        ResultOverlay(
            isVisible = uiState.isResultVisible,
            isLoading = uiState.isLoading,
            result = uiState.result,
            onDismiss = { viewModel.closeResult() }
        )
    }
}
