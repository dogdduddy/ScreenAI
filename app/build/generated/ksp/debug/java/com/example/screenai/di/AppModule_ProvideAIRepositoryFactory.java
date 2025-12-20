package com.example.screenai.di;

import com.example.screenai.data.api.OpenAIApi;
import com.example.screenai.data.local.PreferencesManager;
import com.example.screenai.data.repository.AIRepository;
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
public final class AppModule_ProvideAIRepositoryFactory implements Factory<AIRepository> {
  private final Provider<OpenAIApi> openAIApiProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public AppModule_ProvideAIRepositoryFactory(Provider<OpenAIApi> openAIApiProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.openAIApiProvider = openAIApiProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public AIRepository get() {
    return provideAIRepository(openAIApiProvider.get(), preferencesManagerProvider.get());
  }

  public static AppModule_ProvideAIRepositoryFactory create(Provider<OpenAIApi> openAIApiProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new AppModule_ProvideAIRepositoryFactory(openAIApiProvider, preferencesManagerProvider);
  }

  public static AIRepository provideAIRepository(OpenAIApi openAIApi,
      PreferencesManager preferencesManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAIRepository(openAIApi, preferencesManager));
  }
}
