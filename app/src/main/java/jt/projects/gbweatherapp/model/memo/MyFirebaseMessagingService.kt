package jt.projects.gbweatherapp.model.memo

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import jt.projects.gbweatherapp.utils.CHANNEL_LOW_ID
import jt.projects.gbweatherapp.utils.NOTIFICATION_KEY_MESSAGE
import jt.projects.gbweatherapp.utils.NOTIFICATION_KEY_TITLE
import jt.projects.gbweatherapp.utils.TAG

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // вызывается 1 раз !!
        Notifications.pushNotification("Получен token приложения", token, CHANNEL_LOW_ID)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, message.toString())
        val data = message.data
        val title = data[NOTIFICATION_KEY_TITLE]
        val body = data[NOTIFICATION_KEY_MESSAGE]
        if (!title.isNullOrEmpty() && !body.isNullOrEmpty()) {
            Notifications.pushNotification(title, body, CHANNEL_LOW_ID)
        }
        super.onMessageReceived(message)
    }

}