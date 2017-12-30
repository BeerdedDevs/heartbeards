package io.beerdeddevs.heartbeards.io.config

import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatDelegate.NightMode
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.beerdeddevs.heartbeards.BuildConfig
import java.util.concurrent.TimeUnit

class FirebaseConfig : Config {

  private val cacheExpiration = if (BuildConfig.DEBUG) 1 else TimeUnit.HOURS.toSeconds(12)

  private val config by lazy {
    FirebaseRemoteConfig.getInstance().apply {
      setConfigSettings(FirebaseRemoteConfigSettings.Builder()
              .setDeveloperModeEnabled(BuildConfig.DEBUG)
              .build())
    }
  }

  override fun fetch() {
    config.fetch(cacheExpiration)
        .addOnSuccessListener { config.activateFetched() }
        .addOnFailureListener { /** Do nothing. */ }
  }

  @ColorInt override fun getGalleryIconColor() = config.getColor("gallery_icon_color", Color.BLACK)

  @ColorInt override fun getCameraIconColor() = config.getColor("camera_icon_color", Color.BLACK)

  @NightMode override fun getNightModeValue() = config.getLong("night_mode").toInt()

}

@ColorInt private fun FirebaseRemoteConfig.getColor(key: String, @ColorInt default: Int) = try {
  Color.parseColor(getString(key))
} catch (ignore: Exception) {
  default
}
