package io.beerdeddevs.heartbeards.feature.picture.camera

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import io.beerdeddevs.heartbeards.R
import io.beerdeddevs.heartbeards.feature.common.BeardLoading
import io.beerdeddevs.heartbeards.feature.timeline.TimelineItem
import io.beerdeddevs.heartbeards.timelineReference
import io.fotoapparat.Fotoapparat
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.parameter.selector.LensPositionSelectors.front
import io.fotoapparat.parameter.selector.SizeSelectors.biggestSize
import io.fotoapparat.view.CameraView
import java.io.File

class CameraActivity : AppCompatActivity() {
    @BindView(R.id.activityCameraCameraView) internal lateinit var cameraView: CameraView

    private lateinit var fotoapparat: Fotoapparat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_camera)
        CameraActivity_ViewBinding(this)

        fotoapparat = Fotoapparat
                .with(application)
                .into(cameraView)
                .previewScaleType(ScaleType.CENTER_CROP)
                .photoSize(biggestSize())
                .lensPosition(front())
                .build()
    }

    override fun onStart() {
        super.onStart()
        fotoapparat.start()
    }

    override fun onStop() {
        super.onStop()
        fotoapparat.stop()
    }

    @OnClick(R.id.activityCameraTakePicture) internal fun onTakePictureClicked() {
        val pushItem = timelineReference.push()
        val uniqueId = pushItem.key
        val photoResult = fotoapparat.takePicture()
        val file = File(filesDir, "${System.currentTimeMillis()}.png")

        displayProgress()
        photoResult.saveToFile(file)
                .whenAvailable {
            val riversRef = FirebaseStorage.getInstance().reference.child("images/$uniqueId.jpg")

            riversRef.putFile(Uri.fromFile(file))
                    .addOnSuccessListener({ taskSnapshot ->
                        // Get a URL to the uploaded content
                        val displayName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonymous"
                        val insertedItem = TimelineItem(uniqueId, -System.currentTimeMillis(), displayName,
                                taskSnapshot.downloadUrl.toString())
                        pushItem.setValue(insertedItem)

                        Log.d("Image Upload", "Upload successfull")
                        setResult(Activity.RESULT_OK)
                        finish()
                    })
                    .addOnFailureListener({
                        // Handle unsuccessful uploads
                        // ...
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                        Toast.makeText(this, R.string.upload_failed, Toast.LENGTH_SHORT).show()
                    })
                    .addOnCompleteListener({
                        dismissProgress()
                    })
        }
    }

    private val progressDialog: DialogFragment by lazy { BeardLoading() }

    private fun displayProgress() {
        progressDialog.show(supportFragmentManager, "BeardDialog")
    }

    private fun dismissProgress() {
        progressDialog.dismiss()
    }

}
