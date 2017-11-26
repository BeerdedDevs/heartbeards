package io.beerdeddevs.heartbeards.preferences


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

abstract class BasePrefs constructor(ctx: Context?, name: String) {
    val context: Context
    val prefs: SharedPreferences
    val sharedPrefs: SharedPreferences

    init {
        if (ctx == null) {
            throw IllegalArgumentException("Param context cannot be null")
        }

        context = ctx.applicationContext
        prefs = context.getSharedPreferences(name, Context.MODE_MULTI_PROCESS)
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun edit(key: String, value: String) {
        val edit = prefs.edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun edit(key: String, value: Boolean) {
        val edit = prefs.edit()
        edit.putBoolean(key, value)
        edit.apply()
    }

    fun edit(key: String, value: Int) {
        val edit = prefs.edit()
        edit.putInt(key, value)
        edit.apply()
    }

    fun edit(key: String, value: Long) {
        val edit = prefs.edit()
        edit.putLong(key, value)
        edit.apply()
    }

    fun clean() {
        val edit = prefs.edit()
        edit.clear()
        edit.apply()
    }

    fun registerOnPrefsChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnPrefsChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
