package com.zeroone.wallpaperdeal.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import org.jetbrains.annotations.NotNull

@Dao
interface WallpaperDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: Wallpaper)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpapers(wallpapers: List<Wallpaper>)

    @Delete
    suspend fun deleteWallpaper(wallpaper: Wallpaper)

    @Delete
    suspend fun deleteWallpapers(wallpapers: List<Wallpaper>)

    @Query("SELECT * FROM wallpapers")
    suspend fun getAllWallpapers(): List<Wallpaper> // Null olabilecek bir değer döndürmek yerine direk Wallpaper türünde bir liste döndürülmeli
}


