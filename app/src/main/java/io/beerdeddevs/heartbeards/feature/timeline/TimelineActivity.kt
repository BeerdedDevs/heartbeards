package io.beerdeddevs.heartbeards.feature.timeline

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import butterknife.OnClick
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import io.beerdeddevs.heartbeards.R
import io.beerdeddevs.heartbeards.feature.picture.choose.BottomSheetChoosePicture
import io.beerdeddevs.heartbeards.feature.signup.welcome.WelcomeActivity
import com.firebase.ui.database.FirebaseRecyclerOptions
import io.beerdeddevs.heartbeards.timelineReference

const val REQUEST_CODE_SIGN_IN = 111

class TimelineActivity : AppCompatActivity() {

    private lateinit var adapter: TimelineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
        ButterKnife.bind(this)

        val options = FirebaseRecyclerOptions.Builder<TimelineItem>()
                .setQuery(timelineReference.limitToLast(50), TimelineItem::class.java)
                .build()

        adapter = TimelineAdapter(this@TimelineActivity, options)
        findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(this@TimelineActivity)
            adapter = this@TimelineActivity.adapter
        }
    }

    @OnClick(R.id.addImageFab) fun addImageFabClicked() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivityForResult(Intent(this@TimelineActivity, WelcomeActivity::class.java),
                    REQUEST_CODE_SIGN_IN)
        } else {
            BottomSheetChoosePicture().show(this@TimelineActivity)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SIGN_IN && resultCode == Activity.RESULT_OK) {
            BottomSheetChoosePicture().show(this@TimelineActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if (FirebaseAuth.getInstance().currentUser == null) {
            super.onCreateOptionsMenu(menu)
        } else {
            menuInflater.inflate(R.menu.timeline_menu, menu)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.logout_menu_item -> {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            //TOOD: Do something?
                            item.isVisible = false
                            Log.d("Logout", "User logged out")
                        }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}