package com.example.screenai.di

import android.content.Context
import com.example.screenai.overlay.window.MyLifecycleOwner
import com.example.screenai.overlay.window.OverlayComposeWindow
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WindowModule {

    @Provides
    @Singleton
    fun provideMyLifecycleOwner(): MyLifecycleOwner {
        return MyLifecycleOwner()
    }

    @Provides
    @Singleton
    fun provideOverlayComposeWindow(
        @ApplicationContext context: Context,
        lifecycleOwner: MyLifecycleOwner
    ): OverlayComposeWindow {
        return OverlayComposeWindow(context, lifecycleOwner)
    }
}
