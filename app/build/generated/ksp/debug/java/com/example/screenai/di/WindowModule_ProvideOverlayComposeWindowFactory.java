package com.example.screenai.di;

import android.content.Context;
import com.example.screenai.overlay.window.MyLifecycleOwner;
import com.example.screenai.overlay.window.OverlayComposeWindow;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class WindowModule_ProvideOverlayComposeWindowFactory implements Factory<OverlayComposeWindow> {
  private final Provider<Context> contextProvider;

  private final Provider<MyLifecycleOwner> lifecycleOwnerProvider;

  public WindowModule_ProvideOverlayComposeWindowFactory(Provider<Context> contextProvider,
      Provider<MyLifecycleOwner> lifecycleOwnerProvider) {
    this.contextProvider = contextProvider;
    this.lifecycleOwnerProvider = lifecycleOwnerProvider;
  }

  @Override
  public OverlayComposeWindow get() {
    return provideOverlayComposeWindow(contextProvider.get(), lifecycleOwnerProvider.get());
  }

  public static WindowModule_ProvideOverlayComposeWindowFactory create(
      Provider<Context> contextProvider, Provider<MyLifecycleOwner> lifecycleOwnerProvider) {
    return new WindowModule_ProvideOverlayComposeWindowFactory(contextProvider, lifecycleOwnerProvider);
  }

  public static OverlayComposeWindow provideOverlayComposeWindow(Context context,
      MyLifecycleOwner lifecycleOwner) {
    return Preconditions.checkNotNullFromProvides(WindowModule.INSTANCE.provideOverlayComposeWindow(context, lifecycleOwner));
  }
}
