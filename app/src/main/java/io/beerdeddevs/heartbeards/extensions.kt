package io.beerdeddevs.heartbeards

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.TextView
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
