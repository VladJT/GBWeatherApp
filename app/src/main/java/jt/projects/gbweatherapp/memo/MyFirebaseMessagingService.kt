package jt.projects.gbweatherapp.memo

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // вызывается 1 раз !!
    override fun onNewToken(token: String) {
        Log.d("@@@", "Refreshed token: $token")
        this.pushNotification("Получен token приложения", token)
    }

}