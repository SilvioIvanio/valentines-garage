package com.nust.valentinegarage;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nust.valentinegarage.core.data.di.DataModule_ProvideAuthRepositoryFactory;
import com.nust.valentinegarage.core.data.di.DataModule_ProvideCheckInRepositoryFactory;
import com.nust.valentinegarage.core.data.di.DataModule_ProvideFirebaseAuthFactory;
import com.nust.valentinegarage.core.data.di.DataModule_ProvideFirebaseFirestoreFactory;
import com.nust.valentinegarage.core.data.di.DataModule_ProvideTaskRepositoryFactory;
import com.nust.valentinegarage.core.data.di.DataModule_ProvideThemeRepositoryFactory;
import com.nust.valentinegarage.core.domain.repository.AuthRepository;
import com.nust.valentinegarage.core.domain.repository.CheckInRepository;
import com.nust.valentinegarage.core.domain.repository.TaskRepository;
import com.nust.valentinegarage.core.domain.repository.ThemeRepository;
import com.nust.valentinegarage.feature.admin.AdminViewModel;
import com.nust.valentinegarage.feature.admin.AdminViewModel_HiltModules;
import com.nust.valentinegarage.feature.admin.AuditViewModel;
import com.nust.valentinegarage.feature.admin.AuditViewModel_HiltModules;
import com.nust.valentinegarage.feature.auth.LoginViewModel;
import com.nust.valentinegarage.feature.auth.LoginViewModel_HiltModules;
import com.nust.valentinegarage.feature.checkin.NewIntakeViewModel;
import com.nust.valentinegarage.feature.checkin.NewIntakeViewModel_HiltModules;
import com.nust.valentinegarage.feature.mechanic.MechanicViewModel;
import com.nust.valentinegarage.feature.mechanic.MechanicViewModel_HiltModules;
import com.nust.valentinegarage.feature.mechanic.ServiceDetailViewModel;
import com.nust.valentinegarage.feature.mechanic.ServiceDetailViewModel_HiltModules;
import com.nust.valentinegarage.feature.profile.ProfileViewModel;
import com.nust.valentinegarage.feature.profile.ProfileViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerValentineGarageApp_HiltComponents_SingletonC {
  private DaggerValentineGarageApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public ValentineGarageApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements ValentineGarageApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public ValentineGarageApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements ValentineGarageApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public ValentineGarageApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements ValentineGarageApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public ValentineGarageApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements ValentineGarageApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ValentineGarageApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements ValentineGarageApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ValentineGarageApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements ValentineGarageApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public ValentineGarageApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements ValentineGarageApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public ValentineGarageApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends ValentineGarageApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends ValentineGarageApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends ValentineGarageApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends ValentineGarageApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
      injectMainActivity2(arg0);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>builderWithExpectedSize(7).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_admin_AdminViewModel, AdminViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_admin_AuditViewModel, AuditViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_auth_LoginViewModel, LoginViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_mechanic_MechanicViewModel, MechanicViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_checkin_NewIntakeViewModel, NewIntakeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_profile_ProfileViewModel, ProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_mechanic_ServiceDetailViewModel, ServiceDetailViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectAuthRepository(instance, singletonCImpl.provideAuthRepositoryProvider.get());
      MainActivity_MembersInjector.injectThemeRepository(instance, singletonCImpl.provideThemeRepositoryProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_nust_valentinegarage_feature_profile_ProfileViewModel = "com.nust.valentinegarage.feature.profile.ProfileViewModel";

      static String com_nust_valentinegarage_feature_checkin_NewIntakeViewModel = "com.nust.valentinegarage.feature.checkin.NewIntakeViewModel";

      static String com_nust_valentinegarage_feature_auth_LoginViewModel = "com.nust.valentinegarage.feature.auth.LoginViewModel";

      static String com_nust_valentinegarage_feature_admin_AdminViewModel = "com.nust.valentinegarage.feature.admin.AdminViewModel";

      static String com_nust_valentinegarage_feature_mechanic_MechanicViewModel = "com.nust.valentinegarage.feature.mechanic.MechanicViewModel";

      static String com_nust_valentinegarage_feature_admin_AuditViewModel = "com.nust.valentinegarage.feature.admin.AuditViewModel";

      static String com_nust_valentinegarage_feature_mechanic_ServiceDetailViewModel = "com.nust.valentinegarage.feature.mechanic.ServiceDetailViewModel";

      @KeepFieldType
      ProfileViewModel com_nust_valentinegarage_feature_profile_ProfileViewModel2;

      @KeepFieldType
      NewIntakeViewModel com_nust_valentinegarage_feature_checkin_NewIntakeViewModel2;

      @KeepFieldType
      LoginViewModel com_nust_valentinegarage_feature_auth_LoginViewModel2;

      @KeepFieldType
      AdminViewModel com_nust_valentinegarage_feature_admin_AdminViewModel2;

      @KeepFieldType
      MechanicViewModel com_nust_valentinegarage_feature_mechanic_MechanicViewModel2;

      @KeepFieldType
      AuditViewModel com_nust_valentinegarage_feature_admin_AuditViewModel2;

      @KeepFieldType
      ServiceDetailViewModel com_nust_valentinegarage_feature_mechanic_ServiceDetailViewModel2;
    }
  }

  private static final class ViewModelCImpl extends ValentineGarageApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AdminViewModel> adminViewModelProvider;

    private Provider<AuditViewModel> auditViewModelProvider;

    private Provider<LoginViewModel> loginViewModelProvider;

    private Provider<MechanicViewModel> mechanicViewModelProvider;

    private Provider<NewIntakeViewModel> newIntakeViewModelProvider;

    private Provider<ProfileViewModel> profileViewModelProvider;

    private Provider<ServiceDetailViewModel> serviceDetailViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.adminViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.auditViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.loginViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.mechanicViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.newIntakeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.serviceDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(7).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_admin_AdminViewModel, ((Provider) adminViewModelProvider)).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_admin_AuditViewModel, ((Provider) auditViewModelProvider)).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_auth_LoginViewModel, ((Provider) loginViewModelProvider)).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_mechanic_MechanicViewModel, ((Provider) mechanicViewModelProvider)).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_checkin_NewIntakeViewModel, ((Provider) newIntakeViewModelProvider)).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_profile_ProfileViewModel, ((Provider) profileViewModelProvider)).put(LazyClassKeyProvider.com_nust_valentinegarage_feature_mechanic_ServiceDetailViewModel, ((Provider) serviceDetailViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_nust_valentinegarage_feature_mechanic_MechanicViewModel = "com.nust.valentinegarage.feature.mechanic.MechanicViewModel";

      static String com_nust_valentinegarage_feature_admin_AuditViewModel = "com.nust.valentinegarage.feature.admin.AuditViewModel";

      static String com_nust_valentinegarage_feature_profile_ProfileViewModel = "com.nust.valentinegarage.feature.profile.ProfileViewModel";

      static String com_nust_valentinegarage_feature_admin_AdminViewModel = "com.nust.valentinegarage.feature.admin.AdminViewModel";

      static String com_nust_valentinegarage_feature_auth_LoginViewModel = "com.nust.valentinegarage.feature.auth.LoginViewModel";

      static String com_nust_valentinegarage_feature_mechanic_ServiceDetailViewModel = "com.nust.valentinegarage.feature.mechanic.ServiceDetailViewModel";

      static String com_nust_valentinegarage_feature_checkin_NewIntakeViewModel = "com.nust.valentinegarage.feature.checkin.NewIntakeViewModel";

      @KeepFieldType
      MechanicViewModel com_nust_valentinegarage_feature_mechanic_MechanicViewModel2;

      @KeepFieldType
      AuditViewModel com_nust_valentinegarage_feature_admin_AuditViewModel2;

      @KeepFieldType
      ProfileViewModel com_nust_valentinegarage_feature_profile_ProfileViewModel2;

      @KeepFieldType
      AdminViewModel com_nust_valentinegarage_feature_admin_AdminViewModel2;

      @KeepFieldType
      LoginViewModel com_nust_valentinegarage_feature_auth_LoginViewModel2;

      @KeepFieldType
      ServiceDetailViewModel com_nust_valentinegarage_feature_mechanic_ServiceDetailViewModel2;

      @KeepFieldType
      NewIntakeViewModel com_nust_valentinegarage_feature_checkin_NewIntakeViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.nust.valentinegarage.feature.admin.AdminViewModel 
          return (T) new AdminViewModel(singletonCImpl.provideCheckInRepositoryProvider.get(), singletonCImpl.provideAuthRepositoryProvider.get());

          case 1: // com.nust.valentinegarage.feature.admin.AuditViewModel 
          return (T) new AuditViewModel(singletonCImpl.provideCheckInRepositoryProvider.get(), singletonCImpl.provideTaskRepositoryProvider.get(), singletonCImpl.provideAuthRepositoryProvider.get());

          case 2: // com.nust.valentinegarage.feature.auth.LoginViewModel 
          return (T) new LoginViewModel(singletonCImpl.provideAuthRepositoryProvider.get());

          case 3: // com.nust.valentinegarage.feature.mechanic.MechanicViewModel 
          return (T) new MechanicViewModel(singletonCImpl.provideCheckInRepositoryProvider.get(), singletonCImpl.provideTaskRepositoryProvider.get());

          case 4: // com.nust.valentinegarage.feature.checkin.NewIntakeViewModel 
          return (T) new NewIntakeViewModel(singletonCImpl.provideCheckInRepositoryProvider.get(), singletonCImpl.provideTaskRepositoryProvider.get(), singletonCImpl.provideAuthRepositoryProvider.get());

          case 5: // com.nust.valentinegarage.feature.profile.ProfileViewModel 
          return (T) new ProfileViewModel(singletonCImpl.provideAuthRepositoryProvider.get(), singletonCImpl.provideThemeRepositoryProvider.get());

          case 6: // com.nust.valentinegarage.feature.mechanic.ServiceDetailViewModel 
          return (T) new ServiceDetailViewModel(singletonCImpl.provideCheckInRepositoryProvider.get(), singletonCImpl.provideTaskRepositoryProvider.get(), singletonCImpl.provideAuthRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends ValentineGarageApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends ValentineGarageApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends ValentineGarageApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<FirebaseAuth> provideFirebaseAuthProvider;

    private Provider<FirebaseFirestore> provideFirebaseFirestoreProvider;

    private Provider<AuthRepository> provideAuthRepositoryProvider;

    private Provider<ThemeRepository> provideThemeRepositoryProvider;

    private Provider<CheckInRepository> provideCheckInRepositoryProvider;

    private Provider<TaskRepository> provideTaskRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideFirebaseAuthProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseAuth>(singletonCImpl, 1));
      this.provideFirebaseFirestoreProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseFirestore>(singletonCImpl, 2));
      this.provideAuthRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 0));
      this.provideThemeRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ThemeRepository>(singletonCImpl, 3));
      this.provideCheckInRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<CheckInRepository>(singletonCImpl, 4));
      this.provideTaskRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<TaskRepository>(singletonCImpl, 5));
    }

    @Override
    public void injectValentineGarageApp(ValentineGarageApp valentineGarageApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.nust.valentinegarage.core.domain.repository.AuthRepository 
          return (T) DataModule_ProvideAuthRepositoryFactory.provideAuthRepository(singletonCImpl.provideFirebaseAuthProvider.get(), singletonCImpl.provideFirebaseFirestoreProvider.get());

          case 1: // com.google.firebase.auth.FirebaseAuth 
          return (T) DataModule_ProvideFirebaseAuthFactory.provideFirebaseAuth();

          case 2: // com.google.firebase.firestore.FirebaseFirestore 
          return (T) DataModule_ProvideFirebaseFirestoreFactory.provideFirebaseFirestore();

          case 3: // com.nust.valentinegarage.core.domain.repository.ThemeRepository 
          return (T) DataModule_ProvideThemeRepositoryFactory.provideThemeRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.nust.valentinegarage.core.domain.repository.CheckInRepository 
          return (T) DataModule_ProvideCheckInRepositoryFactory.provideCheckInRepository(singletonCImpl.provideFirebaseFirestoreProvider.get());

          case 5: // com.nust.valentinegarage.core.domain.repository.TaskRepository 
          return (T) DataModule_ProvideTaskRepositoryFactory.provideTaskRepository(singletonCImpl.provideFirebaseFirestoreProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
