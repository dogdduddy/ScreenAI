package com.example.screenai.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.screenai.domain.model.CustomPrompt
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "screenai_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    companion object {
        private val API_KEY = stringPreferencesKey("api_key")
        private val CUSTOM_PROMPTS = stringPreferencesKey("custom_prompts")
    }

    // API Key
    val apiKey: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[API_KEY] ?: ""
    }

    suspend fun saveApiKey(key: String) {
        context.dataStore.edit { preferences ->
            preferences[API_KEY] = key
        }
    }

    // Custom Prompts
    val customPrompts: Flow<List<CustomPrompt>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[CUSTOM_PROMPTS] ?: "[]"
        try {
            json.decodeFromString<List<CustomPrompt>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveCustomPrompts(prompts: List<CustomPrompt>) {
        context.dataStore.edit { preferences ->
            preferences[CUSTOM_PROMPTS] = json.encodeToString(prompts)
        }
    }

    suspend fun addCustomPrompt(prompt: CustomPrompt) {
        context.dataStore.edit { preferences ->
            val current = preferences[CUSTOM_PROMPTS] ?: "[]"
            val list = try {
                json.decodeFromString<List<CustomPrompt>>(current).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }
            list.add(prompt)
            preferences[CUSTOM_PROMPTS] = json.encodeToString(list)
        }
    }

    suspend fun deleteCustomPrompt(promptId: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[CUSTOM_PROMPTS] ?: "[]"
            val list = try {
                json.decodeFromString<List<CustomPrompt>>(current).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }
            list.removeAll { it.id == promptId }
            preferences[CUSTOM_PROMPTS] = json.encodeToString(list)
        }
    }
}
