package com.example.screenai.overlay.window

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import com.example.screenai.overlay.ui.OverlayPanelScreen
import com.example.screenai.overlay.viewmodel.OverlayViewModel
import com.example.screenai.presentation.OverlayTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OverlayPanelComposeWindow(
    context: Context,
    lifecycleOwner: MyLifecycleOwner,
    private val viewModel: OverlayViewModel,
    private val screenshotProvider: () -> Bitmap?,
) : ComposeWindow(context, lifecycleOwner) {

    private val _anchor = MutableStateFlow(Anchor(0, 0))
    val anchor: StateFlow<Anchor> = _anchor

    data class Anchor(val x: Int, val y: Int)

    fun setAnchor(x: Int, y: Int) {
        _anchor.value = Anchor(x, y)
    }

    override fun createLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            overlayType(),
            defaultOverlayFlags(),
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 0
        }
    }

    override fun configureComposeView() {
        composeView.setContent {
            OverlayTheme {
                OverlayPanelScreen(
                    viewModel = viewModel,
                    anchorFlow = anchor,
                    onRequestScreenshot = { screenshotProvider() },
                    onUpdateFocusable = { focusable -> updateFocusable(focusable) }
                )
            }
        }
    }

    private fun updateFocusable(focusable: Boolean) {
        params.flags = if (focusable) focusableOverlayFlags() else defaultOverlayFlags()
        if (composeView.isAttachedToWindow) {
            windowManager.updateViewLayout(composeView, params)
        }
    }
}
