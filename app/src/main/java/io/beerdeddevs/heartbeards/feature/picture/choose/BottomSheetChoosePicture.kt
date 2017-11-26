package io.beerdeddevs.heartbeards.feature.picture.choose

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentActivity
import android.view.View
import butterknife.OnClick
import io.beerdeddevs.heartbeards.R

class BottomSheetChoosePicture : BottomSheetDialogFragment() {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

    val view = View.inflate(context, R.layout.bottom_sheet_choose_picture, null)
    BottomSheetChoosePicture_ViewBinding(this, view)

    dialog.setContentView(view)

    return dialog
  }

  @OnClick(R.id.bottomSheetChoosePictureGallery) internal fun onGalleryClicked() {
    dismiss()
  }

  @OnClick(R.id.bottomSheetChoosePictureCam) internal fun onCamClicked() {
    dismiss()
  }

  fun show(fragmentActivity: FragmentActivity) {
    show(fragmentActivity.supportFragmentManager, TAG)
  }

  companion object {
    private const val TAG = "BottomSheetAvatar"
  }
}
