package com.example.screenai.service;

import com.example.screenai.overlay.window.MyLifecycleOwner;
import com.example.screenai.overlay.window.OverlayComposeWindow;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@QualifierMetadata
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
public final class OverlayService_MembersInjector implements MembersInjector<OverlayService> {
  private final Provider<OverlayComposeWindow> composeWindowProvider;

  private final Provider<MyLifecycleOwner> lifecycleOwnerProvider;

  public OverlayService_MembersInjector(Provider<OverlayComposeWindow> composeWindowProvider,
      Provider<MyLifecycleOwner> lifecycleOwnerProvider) {
    this.composeWindowProvider = composeWindowProvider;
    this.lifecycleOwnerProvider = lifecycleOwnerProvider;
  }

  public static MembersInjector<OverlayService> create(
      Provider<OverlayComposeWindow> composeWindowProvider,
      Provider<MyLifecycleOwner> lifecycleOwnerProvider) {
    return new OverlayService_MembersInjector(composeWindowProvider, lifecycleOwnerProvider);
  }

  @Override
  public void injectMembers(OverlayService instance) {
    injectComposeWindow(instance, composeWindowProvider.get());
    injectLifecycleOwner(instance, lifecycleOwnerProvider.get());
  }

  @InjectedFieldSignature("com.example.screenai.service.OverlayService.composeWindow")
  public static void injectComposeWindow(OverlayService instance,
      OverlayComposeWindow composeWindow) {
    instance.composeWindow = composeWindow;
  }

  @InjectedFieldSignature("com.example.screenai.service.OverlayService.lifecycleOwner")
  public static void injectLifecycleOwner(OverlayService instance,
      MyLifecycleOwner lifecycleOwner) {
    instance.lifecycleOwner = lifecycleOwner;
  }
}
