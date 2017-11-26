package io.beerdeddevs.heartbeards.feature.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.beerdeddevs.heartbeards.R

class BeardLoading : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = layoutInflater.inflate(R.layout.fragment_progress, container, false).also {
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }

}