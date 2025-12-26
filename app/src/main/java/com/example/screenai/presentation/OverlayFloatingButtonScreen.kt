package com.example.screenai.overlay.ui

import androidx.compose.runtime.Composable
import com.example.screenai.overlay.viewmodel.OverlayViewModel

@Composable
fun OverlayFloatingButtonScreen(
    viewModel: OverlayViewModel,
    onDragButton: (Float, Float) -> Unit,
    onClickButton: () -> Unit,
) {
    // 버튼 윈도우는 버튼만 렌더링
    FloatingButton(
        isExpanded = false,
        onToggle = { onClickButton() },
        onDrag = { dx, dy -> onDragButton(dx, dy) }
    )
}
