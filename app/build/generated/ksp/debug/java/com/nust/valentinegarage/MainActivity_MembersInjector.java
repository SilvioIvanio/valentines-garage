package com.nust.valentinegarage;

import com.nust.valentinegarage.core.domain.repository.AuthRepository;
import com.nust.valentinegarage.core.domain.repository.ThemeRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "deprecation"
})
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ThemeRepository> themeRepositoryProvider;

  public MainActivity_MembersInjector(Provider<AuthRepository> authRepositoryProvider,
      Provider<ThemeRepository> themeRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.themeRepositoryProvider = themeRepositoryProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<ThemeRepository> themeRepositoryProvider) {
    return new MainActivity_MembersInjector(authRepositoryProvider, themeRepositoryProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectAuthRepository(instance, authRepositoryProvider.get());
    injectThemeRepository(instance, themeRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.nust.valentinegarage.MainActivity.authRepository")
  public static void injectAuthRepository(MainActivity instance, AuthRepository authRepository) {
    instance.authRepository = authRepository;
  }

  @InjectedFieldSignature("com.nust.valentinegarage.MainActivity.themeRepository")
  public static void injectThemeRepository(MainActivity instance, ThemeRepository themeRepository) {
    instance.themeRepository = themeRepository;
  }
}
