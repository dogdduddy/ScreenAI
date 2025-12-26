package com.example.screenai.overlay.window

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service에서 Compose를 사용하기 위한 LifecycleOwner 구현체
 * Activity/Fragment 없이 Compose의 생명주기 관련 기능들을 사용할 수 있게 해줌
 */
@Singleton
class MyLifecycleOwner @Inject constructor() :
    LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner,
    OnBackPressedDispatcherOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val viewModelStoreImpl = ViewModelStore()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val backPressedDispatcher = OnBackPressedDispatcher {
        // 기본 뒤로가기 처리
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val viewModelStore: ViewModelStore
        get() = viewModelStoreImpl

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override val onBackPressedDispatcher: OnBackPressedDispatcher
        get() = backPressedDispatcher

    fun onCreate() {
        savedStateRegistryController.performRestore(Bundle())
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun onStart() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun onResume() {
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    fun onPause() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun onStop() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        viewModelStoreImpl.clear()
    }

    fun addBackPressedCallback(callback: OnBackPressedCallback) {
        backPressedDispatcher.addCallback(callback)
    }
}
