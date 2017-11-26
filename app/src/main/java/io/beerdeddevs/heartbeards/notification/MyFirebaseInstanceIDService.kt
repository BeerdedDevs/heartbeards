package io.beerdeddevs.heartbeards.notification

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(refreshedToken: String?) {
        refreshedToken?.let {
            FirebaseDatabase.getInstance()
                    .reference
                    .child("notificationTokens")
                    .push()
                    .setValue(refreshedToken)
        }
    }


}