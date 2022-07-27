package jt.projects.gbweatherapp.memo

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import jt.projects.gbweatherapp.utils.NOTIFICATION_KEY_MESSAGE
import jt.projects.gbweatherapp.utils.NOTIFICATION_KEY_TITLE
import jt.projects.gbweatherapp.utils.TAG

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // вызывается 1 раз !!
        this.pushNotification("Получен token приложения", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, message.toString())
        val data = message.data
        val title = data[NOTIFICATION_KEY_TITLE]
        val body = data[NOTIFICATION_KEY_MESSAGE]
        if (!title.isNullOrEmpty() && !body.isNullOrEmpty()) {
            this.pushNotification(title, body)
        }
        super.onMessageReceived(message)
    }

}