package io.beerdeddevs.heartbeards.io.config

import android.graphics.Color
import android.graphics.Color.BLACK
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatDelegate.NightMode
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.beerdeddevs.heartbeards.BuildConfig
import java.util.concurrent.TimeUnit
import kotlin.LazyThreadSafetyMode.NONE

class FirebaseConfig : Config {
  private val config by lazy(NONE) { FirebaseRemoteConfig.getInstance() }

  override fun fetch() {
    config.setConfigSettings(FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build())
    config.fetch(if (BuildConfig.DEBUG) 1 else TimeUnit.HOURS.toSeconds(12))
        .addOnSuccessListener { config.activateFetched() }
        .addOnFailureListener { /** Do nothing. */ }
  }

  @ColorInt override fun getGalleryIconColor() = config.getColor("gallery_icon_color", BLACK)

  @ColorInt override fun getCameraIconColor() = config.getColor("camera_icon_color", BLACK)

  @NightMode override fun getNightModeValue() = config.getLong("night_mode").toInt()
}

@ColorInt private fun FirebaseRemoteConfig.getColor(key: String, @ColorInt default: Int) = try {
  Color.parseColor(getString(key))
} catch (ignore: Exception) {
  default
}
