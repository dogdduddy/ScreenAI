package com.example.screenai.domain.model

import kotlinx.serialization.Serializable

/**
 * 커스텀 프롬프트 모델
 */
@Serializable
data class CustomPrompt(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * AI 응답 모델
 */
data class AIResponse(
    val content: String,
    val isSuccess: Boolean,
    val error: String? = null
)

/**
 * 메뉴 아이템 타입
 */
enum class MenuAction {
    EXTRACT_TEXT,       // 텍스트 추출
    CUSTOM_PROMPT,      // 커스텀 프롬프트
    INPUT_PROMPT        // 프롬프트 입력
}
