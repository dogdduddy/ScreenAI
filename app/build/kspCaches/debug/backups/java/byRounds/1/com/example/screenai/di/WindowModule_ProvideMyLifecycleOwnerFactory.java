package com.example.screenai.di;

import com.example.screenai.overlay.window.MyLifecycleOwner;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class WindowModule_ProvideMyLifecycleOwnerFactory implements Factory<MyLifecycleOwner> {
  @Override
  public MyLifecycleOwner get() {
    return provideMyLifecycleOwner();
  }

  public static WindowModule_ProvideMyLifecycleOwnerFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MyLifecycleOwner provideMyLifecycleOwner() {
    return Preconditions.checkNotNullFromProvides(WindowModule.INSTANCE.provideMyLifecycleOwner());
  }

  private static final class InstanceHolder {
    private static final WindowModule_ProvideMyLifecycleOwnerFactory INSTANCE = new WindowModule_ProvideMyLifecycleOwnerFactory();
  }
}
