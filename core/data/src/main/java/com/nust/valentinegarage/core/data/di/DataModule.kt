package com.nust.valentinegarage.core.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nust.valentinegarage.core.data.repository.AuthRepositoryImpl
import com.nust.valentinegarage.core.data.repository.CheckInRepositoryImpl
import com.nust.valentinegarage.core.data.repository.TaskRepositoryImpl
import com.nust.valentinegarage.core.domain.repository.AuthRepository
import com.nust.valentinegarage.core.domain.repository.CheckInRepository
import com.nust.valentinegarage.core.domain.repository.TaskRepository
import com.nust.valentinegarage.core.domain.repository.ThemeRepository
import com.nust.valentinegarage.core.data.repository.ThemeRepositoryImpl
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        val settings = com.google.firebase.firestore.FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(com.google.firebase.firestore.PersistentCacheSettings.newBuilder().build())
            .build()
        firestore.firestoreSettings = settings
        return firestore
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository = AuthRepositoryImpl(auth, firestore)

    @Provides
    @Singleton
    fun provideCheckInRepository(
        firestore: FirebaseFirestore
    ): CheckInRepository = CheckInRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideTaskRepository(
        firestore: FirebaseFirestore
    ): TaskRepository = TaskRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideThemeRepository(
        @ApplicationContext context: Context
    ): ThemeRepository = ThemeRepositoryImpl(context)
}
