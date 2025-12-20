package com.example.screenai.presentation.customPrompt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screenai.data.repository.PromptRepository
import com.example.screenai.domain.model.CustomPrompt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomPromptUiState(
    val prompts: List<CustomPrompt> = emptyList()
)

@HiltViewModel
class CustomPromptViewModel @Inject constructor(
    private val promptRepository: PromptRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomPromptUiState())
    val uiState: StateFlow<CustomPromptUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            promptRepository.customPrompts.collect { prompts ->
                _uiState.update { it.copy(prompts = prompts) }
            }
        }
    }

    fun addPrompt(title: String, content: String) {
        viewModelScope.launch {
            promptRepository.addPrompt(title, content)
        }
    }

    fun deletePrompt(promptId: String) {
        viewModelScope.launch {
            promptRepository.deletePrompt(promptId)
        }
    }
}
