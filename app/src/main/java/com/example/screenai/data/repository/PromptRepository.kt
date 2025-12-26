package com.example.screenai.data.repository

import com.example.screenai.data.local.PreferencesManager
import com.example.screenai.domain.model.CustomPrompt
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromptRepository @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    val customPrompts: Flow<List<CustomPrompt>> = preferencesManager.customPrompts
    val apiKey: Flow<String> = preferencesManager.apiKey

    suspend fun saveApiKey(key: String) {
        preferencesManager.saveApiKey(key)
    }

    suspend fun addPrompt(title: String, content: String) {
        val prompt = CustomPrompt(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content
        )
        preferencesManager.addCustomPrompt(prompt)
    }

    suspend fun deletePrompt(promptId: String) {
        preferencesManager.deleteCustomPrompt(promptId)
    }

    suspend fun updatePrompts(prompts: List<CustomPrompt>) {
        preferencesManager.saveCustomPrompts(prompts)
    }
}
