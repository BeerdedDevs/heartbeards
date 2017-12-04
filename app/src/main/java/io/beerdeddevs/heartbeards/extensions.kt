package io.beerdeddevs.heartbeards

import android.graphics.drawable.Drawable
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
  add(disposable)
}

fun TextView.setTopDrawable(drawable: Drawable?) {
  setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
}

// Kotlin any extension property for the win.
val Any.timelineReference: DatabaseReference by lazy {
  val database = FirebaseDatabase.getInstance()
  database.setPersistenceEnabled(true)
  database.reference.child("timeline")
}
