package io.beerdeddevs.heartbeards.extension

import android.app.Activity
import android.support.v4.app.Fragment
import io.beerdeddevs.heartbeards.di.component.ApplicationComponent

fun Fragment.applicationComponent(): ApplicationComponent = (context as Activity).applicationComponent()
