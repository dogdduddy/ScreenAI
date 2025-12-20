package com.example.screenai.overlay.window;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class OverlayComposeWindow_Factory implements Factory<OverlayComposeWindow> {
  private final Provider<Context> contextProvider;

  private final Provider<MyLifecycleOwner> lifecycleOwnerProvider;

  public OverlayComposeWindow_Factory(Provider<Context> contextProvider,
      Provider<MyLifecycleOwner> lifecycleOwnerProvider) {
    this.contextProvider = contextProvider;
    this.lifecycleOwnerProvider = lifecycleOwnerProvider;
  }

  @Override
  public OverlayComposeWindow get() {
    return newInstance(contextProvider.get(), lifecycleOwnerProvider.get());
  }

  public static OverlayComposeWindow_Factory create(Provider<Context> contextProvider,
      Provider<MyLifecycleOwner> lifecycleOwnerProvider) {
    return new OverlayComposeWindow_Factory(contextProvider, lifecycleOwnerProvider);
  }

  public static OverlayComposeWindow newInstance(Context context, MyLifecycleOwner lifecycleOwner) {
    return new OverlayComposeWindow(context, lifecycleOwner);
  }
}
