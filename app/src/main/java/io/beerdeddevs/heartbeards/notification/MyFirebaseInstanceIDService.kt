package io.beerdeddevs.heartbeards.notification

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import io.beerdeddevs.heartbeards.extension.databaseReference

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.e("MyInstanceIDService", "refreshedToken: $refreshedToken")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(NotificationToken(refreshedToken ?: ""))
    }

    private fun sendRegistrationToServer(refreshedToken: NotificationToken) {
        databaseReference("notificationTokens/${refreshedToken.token}")
                .setValue(true)
    }

    data class NotificationToken(val token: String)

}