package io.beerdeddevs.heartbeards.feature.picture.choose

import android.Manifest.permission.CAMERA
import android.app.Dialog
import android.content.Intent
import android.net.Uri.fromParts
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.vanniktech.rxpermission.Permission.State.DENIED
import com.vanniktech.rxpermission.Permission.State.DENIED_NOT_SHOWN
import com.vanniktech.rxpermission.Permission.State.GRANTED
import com.vanniktech.rxpermission.Permission.State.REVOKED_BY_POLICY
import com.vanniktech.rxpermission.RxPermission
import io.beerdeddevs.heartbeards.BuildConfig
import io.beerdeddevs.heartbeards.R
import io.beerdeddevs.heartbeards.feature.picture.camera.CameraActivity
import io.beerdeddevs.heartbeards.getComponent
import io.beerdeddevs.heartbeards.plusAssign
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BottomSheetChoosePicture : BottomSheetDialogFragment() {
  @Inject internal lateinit var rxPermission: RxPermission

  @BindView(R.id.bottomSheetChoosePicture) lateinit var rootView: View

  private val compositeDisposable = CompositeDisposable()

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

    val view = View.inflate(context, R.layout.bottom_sheet_choose_picture, null)
    BottomSheetChoosePicture_ViewBinding(this, view)

    getComponent().inject(this)

    dialog.setContentView(view)

    return dialog
  }

  @OnClick(R.id.bottomSheetChoosePictureGallery) internal fun onGalleryClicked() {
    dismiss()
  }

  @OnClick(R.id.bottomSheetChoosePictureCamera) internal fun onCameraClicked() {
    compositeDisposable += rxPermission.request(CAMERA)
        .subscribe({
          val state = it.state()
          when (state) {
            GRANTED -> {
              startActivity(Intent(context, CameraActivity::class.java))
              dismiss()
            }
            DENIED -> Snackbar.make(rootView, R.string.camera_permission_denied, Snackbar.LENGTH_LONG).show()
            DENIED_NOT_SHOWN -> {
              Snackbar.make(rootView, R.string.camera_permission_denied_not_shown, Snackbar.LENGTH_LONG)
                  .setAction(R.string.camera_permission_denied_not_shown_action) {
                    startActivity(Intent().apply {
                      action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                      data = fromParts("package", BuildConfig.APPLICATION_ID, null)
                    })
                  }
                  .show()
            }
            REVOKED_BY_POLICY -> Snackbar.make(rootView, R.string.camera_permission_revoked_by_policy, Snackbar.LENGTH_LONG).show()
          }
        }, { throw it })
  }

  override fun onDestroyView() {
    super.onDestroyView()
    compositeDisposable.clear()
  }

  fun show(fragmentActivity: FragmentActivity) {
    show(fragmentActivity.supportFragmentManager, TAG)
  }

  companion object {
    private const val TAG = "BottomSheetAvatar"
  }
}
