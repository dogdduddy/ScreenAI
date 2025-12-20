package com.example.screenai.di;

import com.example.screenai.data.local.PreferencesManager;
import com.example.screenai.data.repository.PromptRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvidePromptRepositoryFactory implements Factory<PromptRepository> {
  private final Provider<PreferencesManager> preferencesManagerProvider;

  public AppModule_ProvidePromptRepositoryFactory(
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public PromptRepository get() {
    return providePromptRepository(preferencesManagerProvider.get());
  }

  public static AppModule_ProvidePromptRepositoryFactory create(
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new AppModule_ProvidePromptRepositoryFactory(preferencesManagerProvider);
  }

  public static PromptRepository providePromptRepository(PreferencesManager preferencesManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePromptRepository(preferencesManager));
  }
}
