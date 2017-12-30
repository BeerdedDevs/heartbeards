package io.beerdeddevs.heartbeards.io.config

import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatDelegate.NightMode

interface Config {

  fun fetch()

  @ColorInt fun getGalleryIconColor(): Int

  @ColorInt fun getCameraIconColor(): Int

  @NightMode fun getNightModeValue(): Int

}
