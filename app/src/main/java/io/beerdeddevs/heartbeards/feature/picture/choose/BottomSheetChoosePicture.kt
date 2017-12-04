package io.beerdeddevs.heartbeards.feature.picture.choose

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.Intent.CATEGORY_OPENABLE
import android.graphics.PorterDuff.Mode.SRC_IN
import android.net.Uri.fromParts
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
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
import com.vanniktech.rxpermission.Permission.State.DENIED
import com.vanniktech.rxpermission.Permission.State.DENIED_NOT_SHOWN
import com.vanniktech.rxpermission.Permission.State.GRANTED
import com.vanniktech.rxpermission.Permission.State.REVOKED_BY_POLICY
import com.vanniktech.rxpermission.RxPermission
import io.beerdeddevs.heartbeards.BuildConfig
import io.beerdeddevs.heartbeards.R
import io.beerdeddevs.heartbeards.R.string
import io.beerdeddevs.heartbeards.feature.common.BeardLoading
import io.beerdeddevs.heartbeards.feature.picture.camera.CameraActivity
import io.beerdeddevs.heartbeards.feature.timeline.TimelineItem
import io.beerdeddevs.heartbeards.getComponent
import io.beerdeddevs.heartbeards.io.config.Config
import io.beerdeddevs.heartbeards.plusAssign
import io.beerdeddevs.heartbeards.setTopDrawable
import io.beerdeddevs.heartbeards.timelineReference
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BottomSheetChoosePicture : BottomSheetDialogFragment() {
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

    getComponent().inject(this)

    dialog.setContentView(view)

    tintIcons(context)
    return dialog
  }

  @OnClick(R.id.bottomSheetChoosePictureGallery) internal fun onGalleryClicked() {
    startActivityForResult(Intent.createChooser(Intent().apply {
      type = "image/*"
      addCategory(CATEGORY_OPENABLE)
      action = ACTION_GET_CONTENT
    }, getString(string.gallery_title)), PICK_IMAGE_REQUEST_CODE)
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

  private val progressDialog: DialogFragment by lazy { BeardLoading() }

  private fun displayProgress() {
    progressDialog.show(fragmentManager, "BeardDialog")
  }

  private fun dismissProgress() {
    progressDialog.dismiss()
  }

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

              Log.d("Image Upload", "Upload successfull")
              dismiss()
            })
            .addOnFailureListener({
              dismiss()
              Toast.makeText(context, R.string.upload_failed, Toast.LENGTH_SHORT).show()
            })
            .addOnCompleteListener({
              dismissProgress()
            })
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
  }
}
