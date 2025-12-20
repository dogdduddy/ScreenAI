package com.example.screenai.data.repository;

import com.example.screenai.data.local.PreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PromptRepository_Factory implements Factory<PromptRepository> {
  private final Provider<PreferencesManager> preferencesManagerProvider;

  public PromptRepository_Factory(Provider<PreferencesManager> preferencesManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public PromptRepository get() {
    return newInstance(preferencesManagerProvider.get());
  }

  public static PromptRepository_Factory create(
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new PromptRepository_Factory(preferencesManagerProvider);
  }

  public static PromptRepository newInstance(PreferencesManager preferencesManager) {
    return new PromptRepository(preferencesManager);
  }
}
