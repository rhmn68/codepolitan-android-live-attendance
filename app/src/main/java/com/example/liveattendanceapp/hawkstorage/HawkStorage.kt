package com.example.liveattendanceapp.hawkstorage

import android.content.Context
import com.example.liveattendanceapp.model.User
import com.orhanobut.hawk.Hawk

class HawkStorage {
    companion object{
        private const val USER_KEY = "user_key"
        private const val TOKEN_KEY = "token_key"
        private val hawkStorage = HawkStorage()

        fun instance(context: Context?): HawkStorage{
            Hawk.init(context).build()
            return hawkStorage
        }
    }

    fun setUser(user: User){
        Hawk.put(USER_KEY, user)
    }

    fun getUser(): User{
        return Hawk.get(USER_KEY)
    }

    fun setToken(token: String){
        Hawk.put(TOKEN_KEY, token)
    }

    fun getToken(): String{
        val rawToken = Hawk.get<String>(TOKEN_KEY)
        val token = rawToken.split("|")
        return token[1]
    }

    fun isLogin(): Boolean{
        if (Hawk.contains(USER_KEY)){
            return true
        }
        return false
    }

    fun deleteAll(){
        Hawk.deleteAll()
    }
}