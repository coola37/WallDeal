package com.zeroone.wallpaperdeal.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.zeroone.wallpaperdeal.data.local.MIGRATION_1_2
import com.zeroone.wallpaperdeal.data.local.UserDao
import com.zeroone.wallpaperdeal.data.local.UserDatabase
import com.zeroone.wallpaperdeal.data.local.WallpaperDao
import com.zeroone.wallpaperdeal.data.local.WallpaperDatabase
import com.zeroone.wallpaperdeal.data.remote.api.UserAPI
import com.zeroone.wallpaperdeal.data.remote.api.WallDealAPI
import com.zeroone.wallpaperdeal.data.remote.api.WallpaperAPI
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import com.zeroone.wallpaperdeal.data.remote.repository.WallDealRepository
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import com.zeroone.wallpaperdeal.data.remote.repository.impl.UserRepositoryImpl
import com.zeroone.wallpaperdeal.data.remote.repository.impl.WallDealRepositoryImpl
import com.zeroone.wallpaperdeal.data.remote.repository.impl.WallpaperRepositoryImpl
import com.zeroone.wallpaperdeal.utils.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideWallDealRepository(api: WallDealAPI) : WallDealRepository {
        return WallDealRepositoryImpl(api = api)
    }

    @Provides
    @Singleton
    fun provideWallpaperDatabase(@ApplicationContext context: Context): WallpaperDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WallpaperDatabase::class.java,
            "wallpaper_database"
        ).addMigrations(MIGRATION_1_2).build()
    }
    @Provides
    fun provideWallpaperDao(wallpaperDatabase: WallpaperDatabase): WallpaperDao {
        return wallpaperDatabase.wallpaperDao()
    }
    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(context, UserDatabase::class.java, "user_database").build()
    }


    @Provides
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }
}