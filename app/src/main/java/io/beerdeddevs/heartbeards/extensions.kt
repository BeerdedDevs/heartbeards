package io.beerdeddevs.heartbeards

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
  add(disposable)
}

fun FirebaseRemoteConfig.getColor(key: String): Int? {
  return try {
    Color.parseColor(getString(key))
  } catch (ignore: Exception) {
    null
  }
}

fun TextView.setTopDrawable(drawable: Drawable?) {
  setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
}

val Activity.timelineReference: DatabaseReference by lazy {
  FirebaseDatabase.getInstance().reference.child("timeline")
}