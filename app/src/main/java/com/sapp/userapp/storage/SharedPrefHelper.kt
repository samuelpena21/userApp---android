package com.sapp.userapp.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sapp.userapp.MyApp
import com.sapp.userapp.api.models.User
import java.util.*

object SharedPrefHelper {
    private const val KEY = "USER_LIST"

    fun saveUser(value: User) {
        val userList = favoriteUserList

        userList.add(value)
        favoriteUserList = userList
    }

    fun getUserIfExist(name: String?): User? {
        var user: User? = null
        val userList: List<User> = favoriteUserList
        for (u in userList) {
            if (u.fullName.contains(name!!)) {
                user = u
                break
            }
        }
        return user
    }

    fun deleteUserIfExist(name: String?) {
        val userList: List<User> = favoriteUserList
        val newList: MutableList<User> = ArrayList()
        for (u in userList) {
            if (!u.fullName.contains(name!!)) {
                newList.add(u)
            }
        }
        favoriteUserList = newList
    }

    var favoriteUserList: MutableList<User>
        get() {
            val sharedPreferences = MyApp.getAppContext().getSharedPreferences(
                MyApp.getAppContext().packageName, Context.MODE_PRIVATE
            )
            val gson = Gson()
            val json = sharedPreferences.getString(KEY, "")
            val type = object : TypeToken<MutableList<User?>?>() {}.type
            val objList = gson.fromJson<MutableList<User>>(json, type)
            return objList ?: ArrayList()
        }
        private set(userList) {
            val sharedPreferences = MyApp.getAppContext().getSharedPreferences(
                MyApp.getAppContext().packageName, Context.MODE_PRIVATE
            )
            val prefsEditor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(userList)
            prefsEditor.putString(KEY, json)
            prefsEditor.apply()
        }
}