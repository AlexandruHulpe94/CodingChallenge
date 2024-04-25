package com.example.codingchallenge.di

import android.content.Context
import androidx.room.Room
import com.example.codingchallenge.data.local.database.AppDatabase
import com.example.codingchallenge.data.remote.UserApi
import com.example.codingchallenge.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "YourAppDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        api: UserApi,
        db: AppDatabase
    ): UserRepository {
        return UserRepository(api, db)
    }
}

