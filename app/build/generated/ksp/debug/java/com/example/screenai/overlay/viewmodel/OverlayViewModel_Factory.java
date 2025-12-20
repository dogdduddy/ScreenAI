package com.example.screenai.overlay.viewmodel;

import com.example.screenai.data.repository.AIRepository;
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
public final class OverlayViewModel_Factory implements Factory<OverlayViewModel> {
  private final Provider<AIRepository> aiRepositoryProvider;

  private final Provider<PromptRepository> promptRepositoryProvider;

  public OverlayViewModel_Factory(Provider<AIRepository> aiRepositoryProvider,
      Provider<PromptRepository> promptRepositoryProvider) {
    this.aiRepositoryProvider = aiRepositoryProvider;
    this.promptRepositoryProvider = promptRepositoryProvider;
  }

  @Override
  public OverlayViewModel get() {
    return newInstance(aiRepositoryProvider.get(), promptRepositoryProvider.get());
  }

  public static OverlayViewModel_Factory create(Provider<AIRepository> aiRepositoryProvider,
      Provider<PromptRepository> promptRepositoryProvider) {
    return new OverlayViewModel_Factory(aiRepositoryProvider, promptRepositoryProvider);
  }

  public static OverlayViewModel newInstance(AIRepository aiRepository,
      PromptRepository promptRepository) {
    return new OverlayViewModel(aiRepository, promptRepository);
  }
}
