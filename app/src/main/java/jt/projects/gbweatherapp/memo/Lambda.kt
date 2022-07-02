@file:Suppress("unused", "RedundantNullableReturnType", "UNUSED_VARIABLE")

package jt.projects.gbweatherapp.memo

class Lambda {
    fun testFuncInterface() {
//        val callback = object : Testable {
//            override fun test() {
//                val i = 100
//            }

        val callback = Testable { val i = 100 }
    }

    fun interface Testable {
        fun test()
    }
}