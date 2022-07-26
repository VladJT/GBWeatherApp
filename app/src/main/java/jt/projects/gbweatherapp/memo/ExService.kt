package jt.projects.gbweatherapp.memo

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder


//class ExService : Service() {
//    // Для связывания Activity и сервиса
//    private val binder: IBinder = ServiceBinder()
//
//    // Для расчёта чисел Фибоначчи
//    private var fibonacci1: Long = 0
//    private var fibonacci2: Long = 1
//
//    // Вызывается только один раз, при создании сервиса
//    override fun onBind(intent: Intent): IBinder {
//        return binder
//    }
//
//    // Обработка (происходит в основном потоке приложения,
//    // о потоках надо позаботиться самим)
//    fun getNextFibonacci(): Long {
//        val result = fibonacci1 + fibonacci2
//        fibonacci1 = fibonacci2
//        fibonacci2 = result
//        return result
//    }
//
//    // Класс связи между клиентом и сервисом
//    inner class ServiceBinder : Binder() {
//        fun getService(): ExService {
//            return this@ExService
//        }
//
//        fun getNextFibonacci(): Long = getService().getNextFibonacci()
//    }
//
//}