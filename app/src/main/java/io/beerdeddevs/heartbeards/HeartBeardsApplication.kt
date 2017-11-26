package io.beerdeddevs.heartbeards

import android.app.Activity
import android.app.Application
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AppCompatDelegate
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.beerdeddevs.heartbeards.di.component.ApplicationComponent
import java.util.concurrent.TimeUnit

class HeartBeardsApplication : Application() {
    val applicationComponent by lazy { ApplicationComponent.create(this) }

    override fun onCreate() {
        super.onCreate()

        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.setConfigSettings(FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build())
        firebaseRemoteConfig.fetch(if (BuildConfig.DEBUG) 1 else TimeUnit.HOURS.toSeconds(12))
            .addOnSuccessListener { firebaseRemoteConfig.activateFetched() }
            .addOnFailureListener { /** Do nothing */ }

        AppCompatDelegate.setDefaultNightMode(firebaseRemoteConfig.getLong("night_mode").toInt())
    }
}

fun Activity.getComponent() = (application as HeartBeardsApplication).applicationComponent
fun BottomSheetDialogFragment.getComponent() = (context as Activity).getComponent()
