package com.example.screenai.overlay.window

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import com.example.screenai.overlay.ui.OverlayScreen
import com.example.screenai.overlay.viewmodel.OverlayViewModel
import com.example.screenai.overlay.viewmodel.getEntryPoint
import com.example.screenai.presentation.OverlayTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverlayComposeWindow @Inject constructor(
    @ApplicationContext context: Context,
    lifecycleOwner: MyLifecycleOwner
) : ComposeWindow(context, lifecycleOwner) {

    private var screenshotProvider: (() -> Bitmap?)? = null
    private lateinit var viewModel: OverlayViewModel
    private var isExpanded = false

    fun setScreenshotProvider(provider: () -> Bitmap?) {
        screenshotProvider = provider
    }

    override fun configureComposeView() {
        viewModel = getEntryPoint(context).overlayViewModel()

        composeView.setContent {
            OverlayTheme {
                OverlayScreen(
                    composeView = composeView,
                    windowManager = windowManager,
                    layoutParams = params,
                    viewModel = viewModel,
                    onRequestScreenshot = { screenshotProvider?.invoke() },
                    onUpdateFocusable = { focusable ->
                        updateFocusable(focusable)
                    },
                    onExpandOverlay = { expand ->
                        updateOverlaySize(expand)
                    }
                )
            }
        }
    }

    override fun showOverlay() {
        if (!composeView.isAttachedToWindow) {
            windowManager.addView(composeView, params)
        }
    }

    override fun createLayoutParams(): WindowManager.LayoutParams {
        // 기본: 플로팅 버튼 크기만 (88dp = 56dp 버튼 + 32dp 패딩)
        val size = (88 * context.resources.displayMetrics.density).toInt()
        return WindowManager.LayoutParams(
            size,
            size,
            overlayType(),
            defaultOverlayFlags(),
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 200
        }
    }

    private fun updateFocusable(focusable: Boolean) {
        params.flags = if (focusable) {
            focusableOverlayFlags()
        } else {
            defaultOverlayFlags()
        }
        if (composeView.isAttachedToWindow) {
            windowManager.updateViewLayout(composeView, params)
        }
    }

    private fun updateOverlaySize(expand: Boolean) {
        if (isExpanded == expand) return
        isExpanded = expand

        if (expand) {
            // 전체 화면으로 확장
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.MATCH_PARENT
            params.x = 0
            params.y = 0
        } else {
            // 플로팅 버튼 크기로 축소
            val size = (88 * context.resources.displayMetrics.density).toInt()
            params.width = size
            params.height = size
            // 위치는 마지막 드래그 위치 유지하거나 기본값
        }

        if (composeView.isAttachedToWindow) {
            windowManager.updateViewLayout(composeView, params)
        }
    }
}
