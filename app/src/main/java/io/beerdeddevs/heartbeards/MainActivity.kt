package io.beerdeddevs.heartbeards

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.beerdeddevs.heartbeards.feature.picture.choose.BottomSheetChoosePicture

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<View>(R.id.activityMainHelloWorld).setOnClickListener {
      BottomSheetChoosePicture().show(this)
    }
  }
}
