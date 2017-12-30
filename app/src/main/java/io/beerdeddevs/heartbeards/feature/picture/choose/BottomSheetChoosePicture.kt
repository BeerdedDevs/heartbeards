package io.beerdeddevs.heartbeards.feature.picture.choose

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff.Mode.SRC_IN
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.support.v7.content.res.AppCompatResources
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.vanniktech.rxpermission.Permission
import com.vanniktech.rxpermission.Permission.State.DENIED
import com.vanniktech.rxpermission.Permission.State.DENIED_NOT_SHOWN
import com.vanniktech.rxpermission.Permission.State.GRANTED
import com.vanniktech.rxpermission.Permission.State.REVOKED_BY_POLICY
import com.vanniktech.rxpermission.RxPermission
import io.beerdeddevs.heartbeards.BuildConfig
import io.beerdeddevs.heartbeards.R
import io.beerdeddevs.heartbeards.extension.applicationComponent
import io.beerdeddevs.heartbeards.extension.plusAssign
import io.beerdeddevs.heartbeards.extension.setTopDrawable
import io.beerdeddevs.heartbeards.extension.timelineReference
import io.beerdeddevs.heartbeards.feature.common.BeardDialogLoading
import io.beerdeddevs.heartbeards.feature.picture.camera.CameraActivity
import io.beerdeddevs.heartbeards.feature.timeline.TimelineItem
import io.beerdeddevs.heartbeards.io.config.Config
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BottomSheetChoosePicture : BottomSheetDialogFragment(), BeardDialogLoading {

  @Inject internal lateinit var rxPermission: RxPermission
  @Inject internal lateinit var config: Config

  @BindView(R.id.bottomSheetChoosePicture) lateinit var rootView: View
  @BindView(R.id.bottomSheetChoosePictureCamera) lateinit var cameraView: TextView
  @BindView(R.id.bottomSheetChoosePictureGallery) lateinit var galleryView: TextView

  private val compositeDisposable = CompositeDisposable()

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

    val context = requireNotNull(context)
    val view = View.inflate(context, R.layout.bottom_sheet_choose_picture, null)
    BottomSheetChoosePicture_ViewBinding(this, view)

    applicationComponent().inject(this)

    dialog.setContentView(view)

    tintIcons(context)
    return dialog
  }

  @OnClick(R.id.bottomSheetChoosePictureGallery) internal fun onGalleryClicked() {
    val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
      addCategory(Intent.CATEGORY_OPENABLE)
      type = MIME_TYPE_IMAGE
    }
    val intentChooser = Intent.createChooser(galleryIntent, getString(R.string.gallery_title))
    startActivityForResult(intentChooser, PICK_IMAGE_REQUEST_CODE)
  }

  @OnClick(R.id.bottomSheetChoosePictureCamera) internal fun onCameraClicked() {
    compositeDisposable += rxPermission.request(CAMERA)
        .subscribe({ permissionResult(it.state()) }, { throw it })
  }

  private fun permissionResult(state: Permission.State) {
    when (state) {
      GRANTED -> onPermissionGranted()
      REVOKED_BY_POLICY -> buildSnackbar(R.string.camera_permission_revoked_by_policy).show()
      DENIED -> buildSnackbar(R.string.camera_permission_denied).show()
      DENIED_NOT_SHOWN -> {
        buildSnackbar(R.string.camera_permission_denied_not_shown)
                .setAction(R.string.camera_permission_denied_not_shown_action, { onSettingsChangeRequired() })
                .show()
      }
    }
  }

  private fun onPermissionGranted() {
    startActivity(Intent(context, CameraActivity::class.java))
    dismiss()
  }

  private fun onSettingsChangeRequired() {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
      data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
    })
  }

  private fun buildSnackbar(@StringRes messageId: Int) = Snackbar.make(rootView, messageId, Snackbar.LENGTH_LONG)

  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    if (requestCode == PICK_IMAGE_REQUEST_CODE && intent != null && resultCode == Activity.RESULT_OK) {
      // TODO: upload the stream to the server

      val pushItem = timelineReference.push()
      val uniqueId = pushItem.key

      displayProgress()
        val riversRef = FirebaseStorage.getInstance().reference.child("images/$uniqueId.jpg")

        riversRef.putFile(intent.data)
            .addOnSuccessListener({ taskSnapshot ->
              // Get a URL to the uploaded content
              val displayName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonymous"
              val insertedItem = TimelineItem(uniqueId, -System.currentTimeMillis(), displayName,
                  taskSnapshot.downloadUrl.toString())
              pushItem.setValue(insertedItem)

              Log.d("Image Upload", "Upload successful")
              dismiss()
            })
            .addOnFailureListener({
              dismiss()
              Toast.makeText(context, R.string.upload_failed, Toast.LENGTH_SHORT).show()
            })
            .addOnCompleteListener({ dismissProgress() })
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    compositeDisposable.clear()
  }

  fun show(fragmentActivity: FragmentActivity) {
    show(fragmentActivity.supportFragmentManager, TAG)
  }

  private fun tintIcons(context: Context) {
    galleryView.setTopDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_gallery)?.mutate()?.apply {
      setColorFilter(config.getGalleryIconColor(), SRC_IN)
    })

    cameraView.setTopDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_camera)?.mutate()?.apply {
      setColorFilter(config.getCameraIconColor(), SRC_IN)
    })
  }

  companion object {
    private const val TAG = "BottomSheetAvatar"
    private const val PICK_IMAGE_REQUEST_CODE = 51324
    private const val MIME_TYPE_IMAGE = "image/*"
  }
}
