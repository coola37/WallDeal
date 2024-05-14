package com.zeroone.wallpaperdeal.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zeroone.wallpaperdeal.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Delete
    suspend fun deleteUser(user: User)

    @Delete
    suspend fun deleteUsers(users: List<User>)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User> // Null olabilecek bir değer döndürmek yerine direk Wallpaper türünde bir liste döndürülmeli

}