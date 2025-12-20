package com.example.screenai.data.repository;

import com.example.screenai.data.api.OpenAIApi;
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
public final class AIRepository_Factory implements Factory<AIRepository> {
  private final Provider<OpenAIApi> openAIApiProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public AIRepository_Factory(Provider<OpenAIApi> openAIApiProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.openAIApiProvider = openAIApiProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public AIRepository get() {
    return newInstance(openAIApiProvider.get(), preferencesManagerProvider.get());
  }

  public static AIRepository_Factory create(Provider<OpenAIApi> openAIApiProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new AIRepository_Factory(openAIApiProvider, preferencesManagerProvider);
  }

  public static AIRepository newInstance(OpenAIApi openAIApi,
      PreferencesManager preferencesManager) {
    return new AIRepository(openAIApi, preferencesManager);
  }
}
