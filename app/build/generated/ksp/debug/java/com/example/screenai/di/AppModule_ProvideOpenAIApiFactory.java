package com.example.screenai.di;

import com.example.screenai.data.api.OpenAIApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class AppModule_ProvideOpenAIApiFactory implements Factory<OpenAIApi> {
  private final Provider<Retrofit> retrofitProvider;

  public AppModule_ProvideOpenAIApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public OpenAIApi get() {
    return provideOpenAIApi(retrofitProvider.get());
  }

  public static AppModule_ProvideOpenAIApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new AppModule_ProvideOpenAIApiFactory(retrofitProvider);
  }

  public static OpenAIApi provideOpenAIApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideOpenAIApi(retrofit));
  }
}
