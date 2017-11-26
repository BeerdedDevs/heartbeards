package io.beerdeddevs.heartbeards.preferences

import android.content.Context

private val PREFS_NAME = "beardprefs"
private val USER_LOGGED_IN_PREF = "logged_in"
private val USER_TOKEN_PREF = "logged_in"

class BeardPrefs(context: Context, prefsName: String = PREFS_NAME) : BasePrefs(context, prefsName) {
    var userLoggedIn: Boolean
        get() = sharedPrefs.getBoolean(USER_LOGGED_IN_PREF, false)
        set(value) = sharedPrefs.edit().putBoolean(USER_LOGGED_IN_PREF, value).apply()

    var userToken: String
        get() = sharedPrefs.getString(USER_TOKEN_PREF, "")
        set(value) = sharedPrefs.edit().putString(USER_TOKEN_PREF, value).apply()
}