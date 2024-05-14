package com.zeroone.wallpaperdeal.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Eskiden olan tabloyu yedekleyelim
        database.execSQL("CREATE TABLE wallpapers_new (" +
                "wallpaper_id TEXT PRIMARY KEY NOT NULL," +
                "owner TEXT," +
                "user_added_favorite TEXT," +
                "description TEXT," +
                "image_url TEXT NOT NULL," +
                "category TEXT NOT NULL," +
                "liked_users TEXT," +
                "like_count INTEGER)")

        // Eski tablodaki verileri yeni tabloya kopyalayalım
        database.execSQL("INSERT INTO wallpapers_new (wallpaper_id, owner, user_added_favorite, description, image_url, category, liked_users, like_count) " +
                "SELECT wallpaper_id, owner, liked_users, description, image_url, category, liked_users, like_count FROM wallpapers")

        // Eskiden olan tabloyu sil
        database.execSQL("DROP TABLE wallpapers")

        // Yeni tabloyu eski tablo adıyla yeniden adlandır
        database.execSQL("ALTER TABLE wallpapers_new RENAME TO wallpapers")
    }
}
