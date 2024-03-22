package com.zeroone.wallpaperdeal.dependencyinjection

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.zeroone.wallpaperdeal.api.UserAPI
import com.zeroone.wallpaperdeal.api.WallDealAPI
import com.zeroone.wallpaperdeal.api.WallpaperAPI
import com.zeroone.wallpaperdeal.repository.UserRepository
import com.zeroone.wallpaperdeal.repository.WallDealRepository
import com.zeroone.wallpaperdeal.repository.WallpaperRepository
import com.zeroone.wallpaperdeal.repository.impl.UserRepositoryImpl
import com.zeroone.wallpaperdeal.repository.impl.WallDealRepositoryImpl
import com.zeroone.wallpaperdeal.repository.impl.WallpaperRepositoryImpl
import com.zeroone.wallpaperdeal.utils.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun provideStorage() :FirebaseStorage{
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideUserAPI() : UserAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: UserAPI) : UserRepository {
        return UserRepositoryImpl(api = api)
    }

    @Provides
    @Singleton
    fun provideWallpaperAPI() : WallpaperAPI {
        return  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WallpaperAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideWallpaperRepository(api: WallpaperAPI) : WallpaperRepository {
        return WallpaperRepositoryImpl(api = api)
    }

    @Provides
    @Singleton
    fun provideWallDealAPI() : WallDealAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WallDealAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideWallDealRepository(api: WallDealAPI) : WallDealRepository{
        return WallDealRepositoryImpl(api = api)
    }
}