package com.amikostudio.comman

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.amikostudio.activities.SplashActivity
import com.amikostudio.fragments.ApplicationGlobal


class PrefsManager {
    val PREFS_FILENAME = "com.amikostudio.prefs"
    private var mSharedPreferences: SharedPreferences
    private var mEditor: SharedPreferences.Editor
    var context: Context

    constructor(context: Context) {
        this.context = context
        mSharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences.edit()

    }

    fun getSessionId(): String {
        return mSharedPreferences.getString("sessionId", "")!!
    }

    fun setSessionId(sessionId: String) {
        mEditor.putString("sessionId", sessionId)
        mEditor.apply()
    }
     //
    fun getProfile(): String {
        return mSharedPreferences.getString("profile","")!!
    }

    fun setProfile(profile: String) {
        mEditor.putString("profile", profile)
        mEditor.apply()
    }

    fun logout() {
        ApplicationGlobal.sessionId = ""
        ApplicationGlobal.profile = null
        setSessionId("")
        setProfile("")
        val i = Intent(context, SplashActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(i)
    }

}