package com.example.screenai.overlay.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screenai.data.repository.AIRepository
import com.example.screenai.data.repository.PromptRepository
import com.example.screenai.domain.model.AIResponse
import com.example.screenai.domain.model.CustomPrompt
import com.example.screenai.domain.model.MenuAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OverlayUiState(
    val isMenuExpanded: Boolean = false,
    val isPromptInputVisible: Boolean = false,
    val isCustomPromptListVisible: Boolean = false,
    val isResultVisible: Boolean = false,
    val isLoading: Boolean = false,
    val promptInput: String = "",
    val result: AIResponse? = null,
    val customPrompts: List<CustomPrompt> = emptyList(),
    val currentScreenshot: Bitmap? = null,
    // 버튼 위치 추가
    val buttonX: Int = 0,
    val buttonY: Int = 200
)

class OverlayViewModel @Inject constructor(
    private val aiRepository: AIRepository,
    private val promptRepository: PromptRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OverlayUiState())
    val uiState: StateFlow<OverlayUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            promptRepository.customPrompts.collect { prompts ->
                _uiState.update { it.copy(customPrompts = prompts) }
            }
        }
    }

    fun updateButtonPosition(dx: Float, dy: Float) {
        _uiState.update {
            it.copy(
                buttonX = it.buttonX + dx.toInt(),
                buttonY = it.buttonY + dy.toInt()
            )
        }
    }

    fun toggleMenu() {
        _uiState.update { 
            it.copy(
                isMenuExpanded = !it.isMenuExpanded,
                isPromptInputVisible = false,
                isCustomPromptListVisible = false
            )
        }
    }

    fun closeMenu() {
        _uiState.update { 
            it.copy(
                isMenuExpanded = false,
                isPromptInputVisible = false,
                isCustomPromptListVisible = false
            )
        }
    }

    fun onMenuAction(action: MenuAction, screenshot: Bitmap?) {
        _uiState.update { it.copy(currentScreenshot = screenshot) }
        
        when (action) {
            MenuAction.EXTRACT_TEXT -> {
                screenshot?.let { extractText(it) }
            }
            MenuAction.CUSTOM_PROMPT -> {
                _uiState.update { 
                    it.copy(
                        isCustomPromptListVisible = true,
                        isMenuExpanded = false
                    )
                }
            }
            MenuAction.INPUT_PROMPT -> {
                _uiState.update { 
                    it.copy(
                        isPromptInputVisible = true,
                        isMenuExpanded = false,
                        promptInput = ""
                    )
                }
            }
        }
    }

    fun updatePromptInput(text: String) {
        _uiState.update { it.copy(promptInput = text) }
    }

    fun submitPrompt() {
        val screenshot = _uiState.value.currentScreenshot
        val prompt = _uiState.value.promptInput
        
        if (screenshot != null && prompt.isNotBlank()) {
            analyzeWithPrompt(screenshot, prompt)
        }
    }

    fun selectCustomPrompt(prompt: CustomPrompt) {
        val screenshot = _uiState.value.currentScreenshot
        if (screenshot != null) {
            analyzeWithPrompt(screenshot, prompt.content)
            _uiState.update { it.copy(isCustomPromptListVisible = false) }
        }
    }

    private fun extractText(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isLoading = true,
                    isMenuExpanded = false,
                    isResultVisible = true
                )
            }
            
            val response = aiRepository.extractText(bitmap)
            
            _uiState.update { 
                it.copy(
                    isLoading = false,
                    result = response
                )
            }
        }
    }

    private fun analyzeWithPrompt(bitmap: Bitmap, prompt: String) {
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isLoading = true,
                    isPromptInputVisible = false,
                    isResultVisible = true
                )
            }
            
            val response = aiRepository.analyzeScreenshot(bitmap, prompt)
            
            _uiState.update { 
                it.copy(
                    isLoading = false,
                    result = response
                )
            }
        }
    }

    fun closeResult() {
        _uiState.update { 
            it.copy(
                isResultVisible = false,
                result = null,
                currentScreenshot = null
            )
        }
    }

    fun closePromptInput() {
        _uiState.update { 
            it.copy(
                isPromptInputVisible = false,
                promptInput = ""
            )
        }
    }

    fun closeCustomPromptList() {
        _uiState.update { it.copy(isCustomPromptListVisible = false) }
    }
}
