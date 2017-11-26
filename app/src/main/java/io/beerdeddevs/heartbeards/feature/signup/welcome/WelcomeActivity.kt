package io.beerdeddevs.heartbeards.feature.signup.welcome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import io.beerdeddevs.heartbeards.R


private val RC_SIGN_IN = 123

class WelcomeActivity : AppCompatActivity() {

    @BindView(R.id.sign_up_parent) internal lateinit var signUpParentLayout: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.sign_up_button)
    fun signUpButtonClicked() {
        Log.d("SignUp", "Button clicked")
        val intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                        listOf(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                        ))
                .build()
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val signUpResponse = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                //TODO: Go to the logged in screen
                showSnackbar(R.string.sign_in_successful)
            } else {
                if (signUpResponse == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled)
                    return
                }

                when (signUpResponse.errorCode) {
                    ErrorCodes.NO_NETWORK -> showSnackbar(R.string.no_internet_connection)
                    ErrorCodes.UNKNOWN_ERROR -> showSnackbar(R.string.unknown_error)
                }
            }
        }
    }

    private fun showSnackbar(resourceId: Int) {
        Snackbar.make(signUpParentLayout, resourceId, Snackbar.LENGTH_SHORT).show()
    }
}
