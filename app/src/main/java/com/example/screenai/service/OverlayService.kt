package com.example.screenai.service

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.example.screenai.R
import com.example.screenai.overlay.viewmodel.OverlayViewModel
import com.example.screenai.overlay.viewmodel.getEntryPoint
import com.example.screenai.overlay.window.FloatingButtonComposeWindow
import com.example.screenai.overlay.window.MyLifecycleOwner
import com.example.screenai.overlay.window.OverlayPanelComposeWindow
import com.example.screenai.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OverlayService : Service() {

    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var imageReader: ImageReader? = null

    private val handler = Handler(Looper.getMainLooper())
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private lateinit var viewModel: OverlayViewModel

    private lateinit var button1Window: FloatingButtonComposeWindow
    private lateinit var button2Window: FloatingButtonComposeWindow
    private lateinit var panelWindow: OverlayPanelComposeWindow

    // 패널이 지금 show 상태인지 추적
    private var isPanelShown: Boolean = false

    companion object {
        const val CHANNEL_ID = "screenai_overlay_channel"
        const val NOTIFICATION_ID = 1001
        const val EXTRA_RESULT_CODE = "result_code"
        const val EXTRA_RESULT_DATA = "result_data"

        fun createStartIntent(
            context: Context,
            resultCode: Int,
            resultData: Intent
        ): Intent {
            return Intent(context, OverlayService::class.java).apply {
                putExtra(EXTRA_RESULT_CODE, resultCode)
                putExtra(EXTRA_RESULT_DATA, resultData)
            }
        }

        fun createStopIntent(context: Context): Intent {
            return Intent(context, OverlayService::class.java).apply {
                action = "STOP"
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP") {
            stopSelf()
            return START_NOT_STICKY
        }

        val resultCode = intent?.getIntExtra(EXTRA_RESULT_CODE, Activity.RESULT_CANCELED)
            ?: Activity.RESULT_CANCELED

        val resultData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(EXTRA_RESULT_DATA, Intent::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra(EXTRA_RESULT_DATA)
        }

        if (resultCode == Activity.RESULT_OK && resultData != null) {
            startForeground(NOTIFICATION_ID, createNotification())
            setupMediaProjection(resultCode, resultData)
            setupOverlayWindows()
        } else {
            stopSelf()
        }

        return START_STICKY
    }

    private fun setupOverlayWindows() {
        // ✅ ViewModel을 서비스에서 1번만 생성해서 3개 윈도우가 공유
        viewModel = getEntryPoint(applicationContext).overlayViewModel()

        // 버튼 윈도우 2개 (각각 별도의 lifecycleOwner)
        button1Window = FloatingButtonComposeWindow(
            context = applicationContext,
            lifecycleOwner = MyLifecycleOwner(),
            viewModel = viewModel,
            initialX = 40,
            initialY = 300,
            onClick = { anchorX, anchorY ->
                // 패널 메뉴 위치 기준점 갱신
                ensurePanelWindow()
                panelWindow.setAnchor(anchorX, anchorY)

                // 메뉴 토글
                viewModel.toggleMenu()
            }
        )

        button2Window = FloatingButtonComposeWindow(
            context = applicationContext,
            lifecycleOwner = MyLifecycleOwner(),
            viewModel = viewModel,
            initialX = 40,
            initialY = 500,
            onClick = { anchorX, anchorY ->
                ensurePanelWindow()
                panelWindow.setAnchor(anchorX, anchorY)
                viewModel.toggleMenu()
            }
        )

        // 버튼은 항상 표시
        button1Window.create()
        button1Window.show()

        button2Window.create()
        button2Window.show()

        // 패널은 필요할 때만 show/hide (create는 미리 해두면 처음 열릴 때 지연이 줄어듦)
        ensurePanelWindow()
        startPanelVisibilityCollector()
    }

    private fun ensurePanelWindow() {
        if (::panelWindow.isInitialized) return

        panelWindow = OverlayPanelComposeWindow(
            context = applicationContext,
            lifecycleOwner = MyLifecycleOwner(),
            viewModel = viewModel,
            screenshotProvider = { captureScreen() }
        )
        panelWindow.create()
        // 처음에는 숨김
        panelWindow.hide()
        isPanelShown = false
    }

    private fun startPanelVisibilityCollector() {
        serviceScope.launch {
            viewModel.uiState.collectLatest { state ->
                val shouldShow =
                    state.isMenuExpanded ||
                            state.isPromptInputVisible ||
                            state.isResultVisible ||
                            state.isCustomPromptListVisible

                if (shouldShow && !isPanelShown) {
                    panelWindow.show()
                    isPanelShown = true
                } else if (!shouldShow && isPanelShown) {
                    panelWindow.hide()
                    isPanelShown = false
                }
            }
        }
    }

    private fun setupMediaProjection(resultCode: Int, resultData: Intent) {
        val projectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mediaProjection = projectionManager.getMediaProjection(resultCode, resultData)

        // Android 14+ 콜백
        mediaProjection?.registerCallback(object : MediaProjection.Callback() {
            override fun onStop() {
                super.onStop()
                virtualDisplay?.release()
                imageReader?.close()
            }
        }, handler)

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(metrics)

        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val density = metrics.densityDpi

        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2)

        virtualDisplay = mediaProjection?.createVirtualDisplay(
            "ScreenAI",
            width,
            height,
            density,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader?.surface,
            null,
            handler
        )
    }

    private fun captureScreen(): Bitmap? {
        return try {
            val image = imageReader?.acquireLatestImage() ?: return null

            val planes = image.planes
            val buffer = planes[0].buffer
            val pixelStride = planes[0].pixelStride
            val rowStride = planes[0].rowStride
            val rowPadding = rowStride - pixelStride * image.width

            val bitmap = Bitmap.createBitmap(
                image.width + rowPadding / pixelStride,
                image.height,
                Bitmap.Config.ARGB_8888
            )
            bitmap.copyPixelsFromBuffer(buffer)
            image.close()

            Bitmap.createBitmap(bitmap, 0, 0, image.width, image.height)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // 회전/화면변화시 compose 재구성 필요하면 호출
        if (::button1Window.isInitialized) button1Window.reconfigureContent()
        if (::button2Window.isInitialized) button2Window.reconfigureContent()
        if (::panelWindow.isInitialized) panelWindow.reconfigureContent()
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceScope.launch {
            // scope cancel은 마지막에
        }

        if (::button1Window.isInitialized) button1Window.destroy()
        if (::button2Window.isInitialized) button2Window.destroy()
        if (::panelWindow.isInitialized) panelWindow.destroy()

        virtualDisplay?.release()
        imageReader?.close()
        mediaProjection?.stop()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notification_channel_description)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle(getString(R.string.notification_title))
        .setContentText(getString(R.string.notification_text))
        .setSmallIcon(R.drawable.ic_notification)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setOngoing(true)
        .setContentIntent(
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        .addAction(
            R.drawable.ic_close,
            "종료",
            PendingIntent.getService(
                this,
                0,
                createStopIntent(this),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        .build()
}
