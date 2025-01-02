package com.zeroone.wallpaperdeal.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.model.UserDTO
import java.lang.reflect.Type

class Converters {
    @TypeConverter
    fun fromUserToString(user: User?): String? {
        if (user == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<User>() {}.type
        return gson.toJson(user, type)
    }

    @TypeConverter
    fun fromStringToUser(value: String?): User? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<User>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringSetToString(set: Set<String>?): String? {
        if (set == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Set<String>>() {}.type
        return gson.toJson(set, type)
    }

    @TypeConverter
    fun fromStringToStringSet(value: String?): Set<String>? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Set<String>>() {}.type
        return gson.fromJson(value, type)
    }

    // Eski dönüştürücüler de dahil ediliyor
    @TypeConverter
    fun fromUserDTOToString(userDTO: UserDTO?): String? {
        if (userDTO == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<UserDTO>() {}.type
        return gson.toJson(userDTO, type)
    }

    @TypeConverter
    fun fromStringToUserDTO(value: String?): UserDTO? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<UserDTO>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringListToString(stringList: List<String>?): String? {
        if (stringList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(stringList, type)
    }

    @TypeConverter
    fun fromStringToStringList(value: String?): List<String>? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromSetLongToString(set: Set<Long>?): String? {
        if (set == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<Set<Long>>() {}.type
        return gson.toJson(set, type)
    }

    @TypeConverter
    fun fromStringToSetLong(value: String?): Set<Long>? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<Set<Long>>() {}.type
        return gson.fromJson(value, type)
    }

    // Yeni dönüştürücüler
    @TypeConverter
    fun fromSetIntegerToString(set: Set<Integer>?): String? {
        if (set == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<Set<Integer>>() {}.type
        return gson.toJson(set, type)
    }

    @TypeConverter
    fun fromStringToSetInteger(value: String?): Set<Integer>? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<Set<Integer>>() {}.type
        return gson.fromJson(value, type)
    }
}
