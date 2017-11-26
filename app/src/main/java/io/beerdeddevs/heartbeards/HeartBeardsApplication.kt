package io.beerdeddevs.heartbeards

import android.app.Activity
import android.app.Application
import android.support.design.widget.BottomSheetDialogFragment
import io.beerdeddevs.heartbeards.di.component.ApplicationComponent

class HeartBeardsApplication : Application() {

    val applicationComponent by lazy { ApplicationComponent.create(this) }

}

fun Activity.getComponent() = (application as HeartBeardsApplication).applicationComponent
fun BottomSheetDialogFragment.getComponent() = (context as Activity).getComponent()
