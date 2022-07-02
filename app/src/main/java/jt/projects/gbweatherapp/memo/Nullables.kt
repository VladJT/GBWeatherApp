@file:Suppress("unused", "RedundantNullableReturnType", "UNUSED_VARIABLE")

package jt.projects.gbweatherapp.memo

class Nullables {

    fun testNullable() {
        val s: String? = "f"
        val rez: String = s ?: ""
        val len: Int = s?.length ?: 0
    }
}