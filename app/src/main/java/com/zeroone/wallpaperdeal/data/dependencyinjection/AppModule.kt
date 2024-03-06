package com.zeroone.wallpaperdeal.data.dependencyinjection

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.zeroone.wallpaperdeal.data.remote.UserAPI
import com.zeroone.wallpaperdeal.data.remote.WallpaperAPI
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import com.zeroone.wallpaperdeal.data.remote.repository.impl.UserRepositoryImpl
import com.zeroone.wallpaperdeal.data.remote.repository.impl.WallpaperRepositoryImpl
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
    fun provideUserRepository(api: UserAPI) : UserRepository{
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
    fun provideWallpaperRepository(api: WallpaperAPI) : WallpaperRepository{
        return WallpaperRepositoryImpl(api = api)
    }
}