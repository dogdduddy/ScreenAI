package com.example.screenai.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIApi {

    @POST("v1/chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: ChatCompletionRequest
    ): ChatCompletionResponse
}

@Serializable
data class ChatCompletionRequest(
    val model: String = "gpt-4o",
    val messages: List<Message>,
    @SerialName("max_tokens")
    val maxTokens: Int = 4096
)

@Serializable
data class Message(
    val role: String,
    val content: List<ContentPart>
)

@Serializable
sealed class ContentPart {
    @Serializable
    @SerialName("text")
    data class Text(
        val type: String = "text",
        val text: String
    ) : ContentPart()

    @Serializable
    @SerialName("image_url")
    data class ImageUrl(
        val type: String = "image_url",
        @SerialName("image_url")
        val imageUrl: ImageUrlContent
    ) : ContentPart()
}

@Serializable
data class ImageUrlContent(
    val url: String,
    val detail: String = "high"
)

@Serializable
data class ChatCompletionResponse(
    val id: String,
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val index: Int,
    val message: ResponseMessage
)

@Serializable
data class ResponseMessage(
    val role: String,
    val content: String
)
