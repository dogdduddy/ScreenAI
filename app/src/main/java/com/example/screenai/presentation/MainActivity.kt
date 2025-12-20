package com.example.screenai.presentation

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.screenai.presentation.customPrompt.CustomPromptScreen
import com.example.screenai.presentation.main.MainScreen
import com.example.screenai.service.OverlayService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var hasOverlayPermission by mutableStateOf(false)
    private var hasScreenCapturePermission by mutableStateOf(false)
    private var isServiceRunning by mutableStateOf(false)

    private var pendingScreenCaptureIntent: Intent? = null

    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        checkPermissions()
    }

    private val screenCaptureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            hasScreenCapturePermission = true
            startOverlayService(result.resultCode, result.data!!)
        } else {
            Toast.makeText(this, "화면 캡처 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "알림 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkPermissions()

        // Android 13+ 알림 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            ScreenAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "main"
                    ) {
                        composable("main") {
                            MainScreen(
                                hasOverlayPermission = hasOverlayPermission,
                                hasScreenCapturePermission = hasScreenCapturePermission,
                                isServiceRunning = isServiceRunning,
                                onRequestOverlayPermission = { requestOverlayPermission() },
                                onRequestScreenCapture = { requestScreenCapture() },
                                onToggleService = { toggleService() },
                                onNavigateToCustomPrompt = { navController.navigate("customPrompt") }
                            )
                        }
                        composable("customPrompt") {
                            CustomPromptScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    private fun checkPermissions() {
        hasOverlayPermission = Settings.canDrawOverlays(this)
        // 화면 캡처 권한은 한 번 허용하면 서비스 실행까지 유지
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        overlayPermissionLauncher.launch(intent)
    }

    private fun requestScreenCapture() {
        if (!hasOverlayPermission) {
            Toast.makeText(this, "먼저 오버레이 권한을 허용해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val projectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        screenCaptureLauncher.launch(projectionManager.createScreenCaptureIntent())
    }

    private fun toggleService() {
        if (isServiceRunning) {
            stopOverlayService()
        } else {
            if (hasOverlayPermission) {
                requestScreenCapture()
            } else {
                Toast.makeText(this, "먼저 권한을 허용해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startOverlayService(resultCode: Int, data: Intent) {
        val serviceIntent = OverlayService.createStartIntent(this, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        isServiceRunning = true
    }

    private fun stopOverlayService() {
        val serviceIntent = OverlayService.createStopIntent(this)
        startService(serviceIntent)
        isServiceRunning = false
        hasScreenCapturePermission = false
    }
}
