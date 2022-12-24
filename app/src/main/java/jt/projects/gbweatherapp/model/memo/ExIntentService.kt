package jt.projects.gbweatherapp.model.memo

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import jt.projects.gbweatherapp.ui.search.BROADCAST_EXTRA
import jt.projects.gbweatherapp.ui.search.TEST_BROADCAST_INTENT_FILTER


const val EX_SERVICE_STRING_EXTRA = "EX_SERVICE_STRING_EXTRA"

// типовой IntentService
class ExIntentService(name: String = "ExIntentService") : IntentService(name) {
    private val TAG = "ExIntentService"
    private fun createLogMessage(message: String) = Log.d(TAG, message)

    //step 1
    override fun onCreate() {
        createLogMessage("onCreate")
        super.onCreate()
    }

    //step 2
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createLogMessage("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    //step 3
    override fun onHandleIntent(intent: Intent?) {
        createLogMessage("onHandleIntent ${intent?.getStringExtra(EX_SERVICE_STRING_EXTRA)}")
        val s = intent?.getStringExtra(EX_SERVICE_STRING_EXTRA)?.toLong()?.times(1000)
        Thread.sleep(s ?: 0)
        intent?.let {
            sendBack("onHandleIntent ${intent?.getStringExtra(EX_SERVICE_STRING_EXTRA)}")
        }
    }

    //Отправка уведомления о завершении сервиса
    private fun sendBack(result: String) {
        val broadcastIntent = Intent(TEST_BROADCAST_INTENT_FILTER)
        broadcastIntent.putExtra(BROADCAST_EXTRA, result)
        //sendBroadcast(broadcastIntent)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    //step 4
    override fun onDestroy() {
        createLogMessage("onDestroy")
        super.onDestroy()
    }
}