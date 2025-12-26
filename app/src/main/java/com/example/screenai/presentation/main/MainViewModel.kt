package com.example.screenai.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screenai.data.repository.PromptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val apiKey: String = "",
    val isApiKeyValid: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val promptRepository: PromptRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            promptRepository.apiKey.collect { key ->
                _uiState.value = _uiState.value.copy(
                    apiKey = key,
                    isApiKeyValid = key.isNotBlank()
                )
            }
        }
    }

    fun updateApiKey(key: String) {
        viewModelScope.launch {
            promptRepository.saveApiKey(key)
        }
    }
}
