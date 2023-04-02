package com.example.testeapp.di

import android.content.Context
import com.example.testeapp.data.PostDao
import com.example.testeapp.data.PostRoom
import com.example.testeapp.data.repositores.LocalRepository
import com.example.testeapp.data.repositores.LocalRepositoryImpl
import com.example.testeapp.data.repositores.RemoteRepository
import com.example.testeapp.data.repositores.RemoteRepositoryImpl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ModuleInjector {

    companion object {
        val baseUrl = "https://jsonplaceholder.typicode.com"
    }

    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor;
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            //.addInterceptor(KeyInterceptor(API_KEY, API_HOST))
            .build()
    }

    @Provides
    fun moshi(): Moshi {
        return Moshi.Builder()
            .build()
    }


    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): PostRoom {
        return PostRoom.getInstance(appContext)
    }

    @Provides
    fun providePostDao(appDatabase: PostRoom): PostDao {
        return appDatabase.postDao()
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(retrofit: Retrofit): RemoteRepository {
        return RemoteRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideLocalRepository(postDao: PostDao): LocalRepository {
        return LocalRepositoryImpl(postDao)
    }
}