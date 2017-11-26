package io.beerdeddevs.heartbeards.feature.timeline

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import io.beerdeddevs.heartbeards.R
import io.beerdeddevs.heartbeards.feature.picture.choose.BottomSheetChoosePicture
import io.beerdeddevs.heartbeards.feature.signup.welcome.WelcomeActivity

class TimelineActivity : AppCompatActivity() {

    private lateinit var adapter: TimelineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        findViewById<FloatingActionButton>(R.id.addImageFab).apply {
            setOnClickListener {
                if (FirebaseAuth.getInstance().currentUser == null) {
                    startActivity(Intent(this@TimelineActivity, WelcomeActivity::class.java))
                } else {
                    BottomSheetChoosePicture().show(this@TimelineActivity)
                }
            }
        }

        adapter = TimelineAdapter(this@TimelineActivity)
        findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(this@TimelineActivity)
            adapter = this@TimelineActivity.adapter
        }

        adapter.addAll(listOf(TimelineItem("Someone1", "http://via.placeholder.com/350x650"),
                TimelineItem("Someone2", "http://via.placeholder.com/500x350"),
                TimelineItem("Someone3", "http://via.placeholder.com/450x750"),
                TimelineItem("Someone4", "http://via.placeholder.com/450x450")))
    }

}