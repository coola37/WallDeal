package com.zeroone.wallpaperdeal.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zeroone.wallpaperdeal.data.model.UserDTO
import com.zeroone.wallpaperdeal.data.model.UserDetail
import java.lang.reflect.Type

class Converters {

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
    fun fromUserDetail(userDetail: UserDetail?): String? {
        return Gson().toJson(userDetail)
    }

    @TypeConverter
    fun toUserDetail(json: String?): UserDetail? {
        return Gson().fromJson(json, UserDetail::class.java)
    }
}