package io.beerdeddevs.heartbeards.feature.picture.camera

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.google.firebase.storage.FirebaseStorage
import io.beerdeddevs.heartbeards.R
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
        val photoResult = fotoapparat.takePicture()
        val file = File(filesDir, "${System.currentTimeMillis()}.png")
        photoResult.saveToFile(file).whenAvailable {
            // TODO: missing upload here
            val fileUri = Uri.fromFile(file)
            val storageRef = FirebaseStorage.getInstance().getReference()
            val riversRef = storageRef.child("images/rivers.jpg")

            riversRef.putFile(fileUri)
                    .addOnSuccessListener({ taskSnapshot ->
                        // Get a URL to the uploaded content
                        val downloadUrl = taskSnapshot.downloadUrl
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
        }
    }
}
