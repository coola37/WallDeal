package com.zeroone.wallpaperdeal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.utils.Converters

@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

/*
@Database(entities = [Wallpaper::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract fun wallpaperDao(): WallpaperDao
}
 */