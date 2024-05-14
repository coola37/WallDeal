package com.zeroone.wallpaperdeal.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigrationFrom1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE wallpapers ADD COLUMN user_added_favorite TEXT NOT NULL DEFAULT ''")
    }
}
