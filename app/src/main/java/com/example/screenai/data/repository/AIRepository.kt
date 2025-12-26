package com.example.screenai.data.repository

import android.graphics.Bitmap
import android.util.Base64
import com.example.screenai.data.api.ChatCompletionRequest
import com.example.screenai.data.api.ContentPart
import com.example.screenai.data.api.ImageUrlContent
import com.example.screenai.data.api.Message
import com.example.screenai.data.api.OpenAIApi
import com.example.screenai.data.local.PreferencesManager
import com.example.screenai.domain.model.AIResponse
import kotlinx.coroutines.flow.first
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIRepository @Inject constructor(
    private val openAIApi: OpenAIApi,
    private val preferencesManager: PreferencesManager
) {
    /**
     * 스크린샷과 프롬프트를 함께 AI에게 전송
     */
    suspend fun analyzeScreenshot(
        bitmap: Bitmap,
        prompt: String
    ): AIResponse {
        return try {
            val apiKey = preferencesManager.apiKey.first()
            if (apiKey.isBlank()) {
                return AIResponse(
                    content = "",
                    isSuccess = false,
                    error = "API 키가 설정되지 않았습니다. 설정에서 API 키를 입력해주세요."
                )
            }

            val base64Image = bitmapToBase64(bitmap)
            
            val request = ChatCompletionRequest(
                model = "gpt-4o",
                messages = listOf(
                    Message(
                        role = "user",
                        content = listOf(
                            ContentPart.Text(text = prompt),
                            ContentPart.ImageUrl(
                                imageUrl = ImageUrlContent(
                                    url = "data:image/jpeg;base64,$base64Image"
                                )
                            )
                        )
                    )
                )
            )

            val response = openAIApi.createChatCompletion(
                authorization = "Bearer $apiKey",
                request = request
            )

            val content = response.choices.firstOrNull()?.message?.content ?: ""
            AIResponse(
                content = content,
                isSuccess = true
            )
        } catch (e: Exception) {
            AIResponse(
                content = "",
                isSuccess = false,
                error = e.message ?: "알 수 없는 오류가 발생했습니다."
            )
        }
    }

    /**
     * 텍스트 추출 전용
     */
    suspend fun extractText(bitmap: Bitmap): AIResponse {
        return analyzeScreenshot(
            bitmap = bitmap,
            prompt = "이 이미지에서 모든 텍스트를 정확하게 추출해주세요. 레이아웃을 최대한 유지하면서 텍스트만 출력해주세요."
        )
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}
