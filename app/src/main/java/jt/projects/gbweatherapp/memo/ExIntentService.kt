package jt.projects.gbweatherapp.memo

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import jt.projects.gbweatherapp.ui.search.BROADCAST_EXTRA
import jt.projects.gbweatherapp.ui.search.TEST_BROADCAST_INTENT_FILTER

private const val TAG = "MainServiceTAG"
const val MAIN_SERVICE_STRING_EXTRA = "MainServiceExtra"
private fun createLogMessage(message: String) = Log.d(TAG, message)

class ExIntentService(name: String = "MainService") : IntentService(name) {
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
        createLogMessage("onHandleIntent ${intent?.getStringExtra(MAIN_SERVICE_STRING_EXTRA)}")
        val s = intent?.getStringExtra(MAIN_SERVICE_STRING_EXTRA)?.toLong()?.times(1000)
        Thread.sleep(s ?: 0)
        intent?.let {
            sendBack("onHandleIntent ${intent?.getStringExtra(MAIN_SERVICE_STRING_EXTRA)}")
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