package io.beerdeddevs.heartbeards

import android.app.Activity
import android.app.Application
import io.beerdeddevs.heartbeards.di.component.ApplicationComponent

class HeartBeardsApplication : Application() {

    val applicationComponent by lazy { ApplicationComponent.create(this) }

}

fun Activity.getComponent() = (this.application as HeartBeardsApplication).applicationComponent