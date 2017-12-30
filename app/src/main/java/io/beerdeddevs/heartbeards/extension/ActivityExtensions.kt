package io.beerdeddevs.heartbeards.extension

import android.app.Activity
import io.beerdeddevs.heartbeards.HeartBeardsApplication
import io.beerdeddevs.heartbeards.di.component.ApplicationComponent

fun Activity.applicationComponent(): ApplicationComponent = (application as HeartBeardsApplication).applicationComponent
