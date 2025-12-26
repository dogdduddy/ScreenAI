package com.example.screenai.overlay.window

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import com.example.screenai.overlay.ui.OverlayFloatingButtonScreen
import com.example.screenai.overlay.viewmodel.OverlayViewModel
import com.example.screenai.presentation.OverlayTheme

class FloatingButtonComposeWindow(
    context: Context,
    lifecycleOwner: MyLifecycleOwner,
    private val viewModel: OverlayViewModel,
    private val initialX: Int,
    private val initialY: Int,
    private val onClick: (anchorX: Int, anchorY: Int) -> Unit,
) : ComposeWindow(context, lifecycleOwner) {

    override fun createLayoutParams(): WindowManager.LayoutParams {
        val size = (88 * context.resources.displayMetrics.density).toInt()
        return WindowManager.LayoutParams(
            size,
            size,
            overlayType(),
            defaultOverlayFlags(),
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = initialX
            y = initialY
        }
    }

    override fun configureComposeView() {
        composeView.setContent {
            OverlayTheme {
                OverlayFloatingButtonScreen(
                    viewModel = viewModel,
                    onDragButton = { dx, dy ->
                        // ✅ 윈도우 자체 이동
                        params.x += dx.toInt()
                        params.y += dy.toInt()
                        if (composeView.isAttachedToWindow) {
                            windowManager.updateViewLayout(composeView, params)
                        }
                    },
                    onClickButton = {
                        // 패널이 이 버튼 기준으로 떠야 하니까 앵커 좌표 전달
                        onClick(params.x, params.y)
                    }
                )
            }
        }
    }
}
