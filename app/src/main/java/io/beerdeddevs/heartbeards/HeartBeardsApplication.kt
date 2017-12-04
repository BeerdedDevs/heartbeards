package io.beerdeddevs.heartbeards

import android.app.Activity
import android.app.Application
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AppCompatDelegate
import io.beerdeddevs.heartbeards.di.component.DaggerApplicationComponent
import io.beerdeddevs.heartbeards.io.config.Config
import javax.inject.Inject

class HeartBeardsApplication : Application() {
  @Inject internal lateinit var config: Config

  val applicationComponent by lazy {
    DaggerApplicationComponent.builder()
        .application(this)
        .build()
  }

  override fun onCreate() {
    super.onCreate()

    applicationComponent.inject(this)

    config.fetch()
    AppCompatDelegate.setDefaultNightMode(config.getNightModeValue())
  }
}

fun Activity.getComponent() = (application as HeartBeardsApplication).applicationComponent
fun BottomSheetDialogFragment.getComponent() = (context as Activity).getComponent()
