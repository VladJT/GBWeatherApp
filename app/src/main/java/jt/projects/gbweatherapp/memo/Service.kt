package jt.projects.gbweatherapp.memo

import android.app.IntentService
import android.content.Intent
import android.util.Log
import jt.projects.gbweatherapp.ui.search.TEST_BROADCAST_INTENT_FILTER
import jt.projects.gbweatherapp.ui.search.THREADS_FRAGMENT_BROADCAST_EXTRA

private const val TAG = "MainServiceTAG"
const val MAIN_SERVICE_STRING_EXTRA = "MainServiceExtra"
private fun createLogMessage(message: String) = Log.d(TAG, message)

class Service(name: String = "MainService") : IntentService(name) {
    //step 1
    override fun onCreate() {
        createLogMessage("onCreate")
        super.onCreate()
    }

    //step 2
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createLogMessage("onStartCommand")
        val s = intent?.getStringExtra(MAIN_SERVICE_STRING_EXTRA)?.toLong()?.times(1000)
        Thread.sleep(s?:0)
        return super.onStartCommand(intent, flags, startId)
    }

    //step 3 - result
    override fun onHandleIntent(intent: Intent?) {
        createLogMessage("onHandleIntent ${intent?.getStringExtra(MAIN_SERVICE_STRING_EXTRA)}")
        intent?.let {
            sendBack(it.getStringExtra(MAIN_SERVICE_STRING_EXTRA).toString())
        }
    }

    //Отправка уведомления о завершении сервиса
    private fun sendBack(result: String) {
        val broadcastIntent = Intent(TEST_BROADCAST_INTENT_FILTER)
        broadcastIntent.putExtra(THREADS_FRAGMENT_BROADCAST_EXTRA, result)
        sendBroadcast(broadcastIntent)
    }

    //step 4
    override fun onDestroy() {
        createLogMessage("onDestroy")
        super.onDestroy()
    }
}