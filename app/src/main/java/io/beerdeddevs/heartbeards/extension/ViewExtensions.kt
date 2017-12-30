package io.beerdeddevs.heartbeards.extension

import android.graphics.drawable.Drawable
import android.widget.TextView

fun TextView.setTopDrawable(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
}
