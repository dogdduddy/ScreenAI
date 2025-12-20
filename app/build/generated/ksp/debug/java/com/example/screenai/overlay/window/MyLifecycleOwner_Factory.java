package com.example.screenai.overlay.window;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class MyLifecycleOwner_Factory implements Factory<MyLifecycleOwner> {
  @Override
  public MyLifecycleOwner get() {
    return newInstance();
  }

  public static MyLifecycleOwner_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MyLifecycleOwner newInstance() {
    return new MyLifecycleOwner();
  }

  private static final class InstanceHolder {
    private static final MyLifecycleOwner_Factory INSTANCE = new MyLifecycleOwner_Factory();
  }
}
