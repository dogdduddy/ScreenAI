package com.example.screenai.presentation.customPrompt;

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
public final class CustomPromptViewModel_Factory implements Factory<CustomPromptViewModel> {
  private final Provider<PromptRepository> promptRepositoryProvider;

  public CustomPromptViewModel_Factory(Provider<PromptRepository> promptRepositoryProvider) {
    this.promptRepositoryProvider = promptRepositoryProvider;
  }

  @Override
  public CustomPromptViewModel get() {
    return newInstance(promptRepositoryProvider.get());
  }

  public static CustomPromptViewModel_Factory create(
      Provider<PromptRepository> promptRepositoryProvider) {
    return new CustomPromptViewModel_Factory(promptRepositoryProvider);
  }

  public static CustomPromptViewModel newInstance(PromptRepository promptRepository) {
    return new CustomPromptViewModel(promptRepository);
  }
}
