package com.example.screenai.di;

import com.example.screenai.data.repository.AIRepository;
import com.example.screenai.data.repository.PromptRepository;
import com.example.screenai.overlay.viewmodel.OverlayViewModel;
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
public final class ViewModelModule_ProvideOverlayViewModelFactory implements Factory<OverlayViewModel> {
  private final Provider<AIRepository> aiRepositoryProvider;

  private final Provider<PromptRepository> promptRepositoryProvider;

  public ViewModelModule_ProvideOverlayViewModelFactory(Provider<AIRepository> aiRepositoryProvider,
      Provider<PromptRepository> promptRepositoryProvider) {
    this.aiRepositoryProvider = aiRepositoryProvider;
    this.promptRepositoryProvider = promptRepositoryProvider;
  }

  @Override
  public OverlayViewModel get() {
    return provideOverlayViewModel(aiRepositoryProvider.get(), promptRepositoryProvider.get());
  }

  public static ViewModelModule_ProvideOverlayViewModelFactory create(
      Provider<AIRepository> aiRepositoryProvider,
      Provider<PromptRepository> promptRepositoryProvider) {
    return new ViewModelModule_ProvideOverlayViewModelFactory(aiRepositoryProvider, promptRepositoryProvider);
  }

  public static OverlayViewModel provideOverlayViewModel(AIRepository aiRepository,
      PromptRepository promptRepository) {
    return Preconditions.checkNotNullFromProvides(ViewModelModule.INSTANCE.provideOverlayViewModel(aiRepository, promptRepository));
  }
}
