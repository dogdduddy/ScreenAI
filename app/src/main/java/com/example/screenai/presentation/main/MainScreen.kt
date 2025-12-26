package com.example.screenai.presentation.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.ScreenshotMonitor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screenai.presentation.Background
import com.example.screenai.presentation.Error
import com.example.screenai.presentation.OnSurface
import com.example.screenai.presentation.OnSurfaceVariant
import com.example.screenai.presentation.Primary
import com.example.screenai.presentation.PrimaryVariant
import com.example.screenai.presentation.Success
import com.example.screenai.presentation.Surface
import com.example.screenai.presentation.SurfaceVariant

@Composable
fun MainScreen(
    hasOverlayPermission: Boolean,
    hasScreenCapturePermission: Boolean,
    isServiceRunning: Boolean,
    onRequestOverlayPermission: () -> Unit,
    onRequestScreenCapture: () -> Unit,
    onToggleService: () -> Unit,
    onNavigateToCustomPrompt: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showApiKeyDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Primary, PrimaryVariant)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "ScreenAI",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "화면을 AI에게 물어보세요",
                    color = OnSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Permission Section
        Text(
            text = "권한 설정",
            color = OnSurfaceVariant,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        PermissionCard(
            icon = Icons.Default.Layers,
            title = "다른 앱 위에 표시",
            description = "플로팅 버튼을 표시하기 위해 필요합니다",
            isGranted = hasOverlayPermission,
            onClick = onRequestOverlayPermission
        )

        Spacer(modifier = Modifier.height(12.dp))

        PermissionCard(
            icon = Icons.Default.ScreenshotMonitor,
            title = "스크린샷 권한",
            description = "화면을 캡처하여 AI에게 전송합니다",
            isGranted = hasScreenCapturePermission,
            onClick = onRequestScreenCapture,
            enabled = hasOverlayPermission
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Menu Section
        Text(
            text = "설정",
            color = OnSurfaceVariant,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        MenuCard(
            icon = Icons.Default.Edit,
            title = "커스텀 프롬프트",
            description = "자주 사용하는 프롬프트를 저장하세요",
            onClick = onNavigateToCustomPrompt
        )

        Spacer(modifier = Modifier.height(12.dp))

        MenuCard(
            icon = Icons.Default.Key,
            title = "API 키 설정",
            description = if (uiState.isApiKeyValid) "API 키가 설정되어 있습니다" else "OpenAI API 키를 입력해주세요",
            onClick = { showApiKeyDialog = true }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Start Button
        val allPermissionsGranted = hasOverlayPermission && uiState.isApiKeyValid

        Button(
            onClick = onToggleService,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = allPermissionsGranted,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isServiceRunning) Error else Primary,
                disabledContainerColor = SurfaceVariant
            )
        ) {
            Text(
                text = when {
                    isServiceRunning -> "서비스 종료"
                    !hasOverlayPermission -> "권한을 먼저 허용해주세요"
                    !uiState.isApiKeyValid -> "API 키를 설정해주세요"
                    else -> "서비스 시작"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    // API Key Dialog
    ApiKeyDialog(
        isVisible = showApiKeyDialog,
        currentKey = uiState.apiKey,
        onDismiss = { showApiKeyDialog = false },
        onSave = { key ->
            viewModel.updateApiKey(key)
            showApiKeyDialog = false
        }
    )
}

@Composable
private fun PermissionCard(
    icon: ImageVector,
    title: String,
    description: String,
    isGranted: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isGranted) Success.copy(alpha = 0.1f) else Surface,
        animationSpec = tween(300),
        label = "bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isGranted) Success.copy(alpha = 0.3f) else Color.Transparent,
        animationSpec = tween(300),
        label = "border"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled && !isGranted) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (isGranted) Success.copy(alpha = 0.15f)
                        else Primary.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isGranted) Icons.Default.Check else icon,
                    contentDescription = null,
                    tint = if (isGranted) Success else Primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = OnSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isGranted) "허용됨" else description,
                    color = if (isGranted) Success else OnSurfaceVariant,
                    fontSize = 13.sp
                )
            }

            if (!isGranted && enabled) {
                Text(
                    text = "허용",
                    color = Primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun MenuCard(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = OnSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    color = OnSurfaceVariant,
                    fontSize = 13.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = OnSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun ApiKeyDialog(
    isVisible: Boolean,
    currentKey: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var apiKey by remember(isVisible) { mutableStateOf(currentKey) }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .imePadding()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "API 키 설정",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "닫기",
                                tint = OnSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "OpenAI API 키를 입력해주세요",
                        color = OnSurfaceVariant,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    BasicTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceVariant)
                            .padding(14.dp),
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 15.sp
                        ),
                        cursorBrush = SolidColor(Primary),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                if (apiKey.isEmpty()) {
                                    Text(
                                        text = "sk-...",
                                        color = OnSurfaceVariant.copy(alpha = 0.5f),
                                        fontSize = 15.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (apiKey.isNotBlank()) Primary
                                else Primary.copy(alpha = 0.3f)
                            )
                            .clickable(enabled = apiKey.isNotBlank()) {
                                onSave(apiKey)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "저장",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
