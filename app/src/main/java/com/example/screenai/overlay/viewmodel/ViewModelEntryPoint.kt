package com.example.screenai.overlay.viewmodel

import android.content.Context
import com.example.screenai.data.repository.PromptRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@EntryPoint
interface ViewModelEntryPoint {
    fun overlayViewModel(): OverlayViewModel
    fun promptRepository(): PromptRepository
}

fun getEntryPoint(context: Context): ViewModelEntryPoint {
    return EntryPointAccessors.fromApplication(
        context.applicationContext,
        ViewModelEntryPoint::class.java
    )
}
