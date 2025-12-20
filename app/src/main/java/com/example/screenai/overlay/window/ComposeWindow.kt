package com.example.screenai.overlay.window

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.isVisible
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * Window 위에 ComposeView를 띄우기 위한 추상 클래스
 */
abstract class ComposeWindow(
    protected val context: Context,
    protected val lifecycleOwner: MyLifecycleOwner
) {
    protected val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    protected val params by lazy { createLayoutParams() }
    protected val composeView by lazy { createComposeView() }
    protected val handler = Handler(Looper.getMainLooper())
    protected val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun create() {
        lifecycleOwner.run {
            onCreate()
            configureComposeView()
            onStart()
            showOverlay()
            onResume()
        }
    }

    fun destroy() {
        if (composeView.isAttachedToWindow) {
            windowManager.removeView(composeView)
        }
        scope.cancel()
        lifecycleOwner.run {
            onPause()
            onStop()
            onDestroy()
        }
    }

    fun reconfigureContent() {
        handler.post {
            configureComposeView()
        }
    }

    protected fun setVisibility(visible: Boolean) {
        if (composeView.isVisible == visible) return
        composeView.isVisible = visible
    }

    private fun createComposeView(): ComposeView = ComposeView(context).apply {
        pivotX = 0f
        pivotY = 0f
        setViewTreeLifecycleOwner(lifecycleOwner)
        setViewTreeViewModelStoreOwner(lifecycleOwner)
        setViewTreeSavedStateRegistryOwner(lifecycleOwner)
    }

    protected abstract fun configureComposeView()
    protected abstract fun showOverlay()
    protected abstract fun createLayoutParams(): WindowManager.LayoutParams

    protected fun defaultOverlayFlags() =
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL

    protected fun focusableOverlayFlags() =
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL

    protected fun overlayType() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        @Suppress("DEPRECATION")
        WindowManager.LayoutParams.TYPE_PHONE
    }

    fun updateLayoutParams(update: WindowManager.LayoutParams.() -> Unit) {
        params.update()
        if (composeView.isAttachedToWindow) {
            windowManager.updateViewLayout(composeView, params)
        }
    }
}
