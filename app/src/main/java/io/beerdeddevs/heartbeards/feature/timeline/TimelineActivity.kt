package io.beerdeddevs.heartbeards.feature.timeline

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.appinvite.AppInviteInvitation
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import io.beerdeddevs.heartbeards.R
import io.beerdeddevs.heartbeards.extension.applicationComponent
import io.beerdeddevs.heartbeards.extension.timelineReference
import io.beerdeddevs.heartbeards.feature.picture.choose.BottomSheetChoosePicture
import javax.inject.Inject

class TimelineActivity : AppCompatActivity() {

    private lateinit var adapter: TimelineAdapter
    @Inject internal lateinit var firebaseAnalytics: FirebaseAnalytics
    @BindView(R.id.activityTimeline) internal lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_timeline)
        ButterKnife.bind(this)

        applicationComponent().inject(this)

        val query = timelineReference.orderByChild("timestamp").limitToLast(50)
        val options = FirebaseRecyclerOptions.Builder<TimelineItem>()
                .setQuery(query, TimelineItem::class.java)
                .build()

        adapter = TimelineAdapter(this, options)
        adapter.setHasStableIds(true)

        findViewById<RecyclerView>(R.id.recycler).apply {
            // TODO: Re-visit this, I don't think we need more than fixedSize
            // Optimizations taken from here - https://stackoverflow.com/a/36919009/1979703
            setHasFixedSize(true)
            setItemViewCacheSize(50)
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH

            layoutManager = LinearLayoutManager(this@TimelineActivity)
            adapter = this@TimelineActivity.adapter
        }
    }

    @OnClick(R.id.addImageFab)
    fun addImageFabClicked() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(
                            listOf(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                    .setLogo(R.mipmap.logo)
                    .setTheme(R.style.AppTheme)
                    .build()
            startActivityForResult(intent, REQUEST_CODE_SIGN_IN)
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

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                onSignInSuccess()
            } else {
                onSignInFail(data)
            }
        } else if (requestCode == REQUEST_INVITE && resultCode == Activity.RESULT_OK) {
            // TODO: we could do something here but firebase already shows a Snackbar.
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.timeline_menu, menu)
        menu.findItem(R.id.logout_menu_item).isVisible = FirebaseAuth.getInstance().currentUser != null
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_menu_item -> {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            // TODO: Do something?
                            item.isVisible = false
                            Log.d("Logout", "User logged out")
                        }
            }
            R.id.share_menu_item -> {
                val title = getString(R.string.invitation_title)
                val intent = AppInviteInvitation.IntentBuilder(title)
                    .setMessage(getString(R.string.invitation_message))
                    .setAndroidMinimumVersionCode(21)
                    .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                    .setCustomImage(Uri.parse(MAGICAL_INVITE_ICON_PATH))
                    .setCallToActionText(getString(R.string.invitation_cta))
                    .build()
                startActivityForResult(intent, REQUEST_INVITE)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun onSignInSuccess() {
        showSnackbar(R.string.sign_in_successful)
        val bundle = Bundle().apply {
            putString("STATUS", "completed")
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)

        BottomSheetChoosePicture().show(this)
        invalidateOptionsMenu()
    }

    private fun onSignInFail(data: Intent?) {
        IdpResponse.fromResultIntent(data)?.let {
            when (it.errorCode) {
                ErrorCodes.NO_NETWORK -> showSnackbar(R.string.no_internet_connection)
                ErrorCodes.UNKNOWN_ERROR -> showSnackbar(R.string.unknown_error)
            }
        } ?: run {
            // User pressed back button
            showSnackbar(R.string.sign_in_cancelled)
        }
    }

    private fun showSnackbar(resourceId: Int) {
        Snackbar.make(rootView, resourceId, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_CODE_SIGN_IN = 111
        private const val REQUEST_INVITE = 112
        private const val MAGICAL_INVITE_ICON_PATH // ¯\_(ツ)_/¯
                = "android.resource://io.beerdeddevs.heartbeards/drawable/firebase_invite_image"
    }

}
