package com.example.screenai.presentation.main;

import com.example.screenai.data.repository.PromptRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<PromptRepository> promptRepositoryProvider;

  public MainViewModel_Factory(Provider<PromptRepository> promptRepositoryProvider) {
    this.promptRepositoryProvider = promptRepositoryProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(promptRepositoryProvider.get());
  }

  public static MainViewModel_Factory create(Provider<PromptRepository> promptRepositoryProvider) {
    return new MainViewModel_Factory(promptRepositoryProvider);
  }

  public static MainViewModel newInstance(PromptRepository promptRepository) {
    return new MainViewModel(promptRepository);
  }
}
