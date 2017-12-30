package io.beerdeddevs.heartbeards.feature.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.beerdeddevs.heartbeards.R

class BeardLoading : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = layoutInflater.inflate(R.layout.fragment_progress, container, false).also {
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}

interface BeardDialogLoading {

    fun displayProgress() {
        if (!progressDialog.isVisible) {
            when (this) {
                is AppCompatActivity -> progressDialog.show(supportFragmentManager, TAG)
                is Fragment -> progressDialog.show(fragmentManager, TAG)
            }
        }
    }

    fun dismissProgress() {
        progressDialog.dismiss()
    }

    companion object {
        const val TAG = "BeardDialog"
    }

}

private val BeardDialogLoading.progressDialog: DialogFragment by lazy { BeardLoading() }