package io.beerdeddevs.heartbeards

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.beerdeddevs.heartbeards.feature.picture.choose.BottomSheetChoosePicture
import io.beerdeddevs.heartbeards.feature.signup.welcome.WelcomeActivity
import io.beerdeddevs.heartbeards.feature.timeline.TimelineActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent().inject(this)
        setContentView (R.layout.activity_main)

        findViewById<View>(R.id.bottomSheetButton).setOnClickListener {
            BottomSheetChoosePicture().show(this)
        }

        findViewById<View>(R.id.welcomeButton).setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }

        findViewById<View>(R.id.timelineButton).setOnClickListener {
            startActivity(Intent(this, TimelineActivity::class.java))
        }
    }
}
