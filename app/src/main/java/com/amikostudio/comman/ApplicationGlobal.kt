package com.amikostudio.fragments

import android.app.Application
import com.amikostudio.comman.PrefsManager
import com.amikostudio.retrofit.Profile
import com.google.gson.Gson

class ApplicationGlobal : Application() {

    companion object {

        lateinit var prefsManager: PrefsManager
        var sessionId: String = ""
        var profile : Profile? = null
        var email : String = ""
        var password : String = ""

        private lateinit var instance: ApplicationGlobal

        @Synchronized
        fun getInstance(): ApplicationGlobal = instance

    }

    override fun onCreate() {
        super.onCreate()
        prefsManager = PrefsManager(this)
        profile = Gson().fromJson(prefsManager.getProfile(), Profile::class.java)
        sessionId = prefsManager.getSessionId()

    }
}