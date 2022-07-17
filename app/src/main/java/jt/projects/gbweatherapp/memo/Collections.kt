@file:Suppress("unused", "RedundantNullableReturnType", "UNUSED_VARIABLE")

package jt.projects.gbweatherapp.memo

class Collections {

    fun testCollections() {
        val b_arr = arrayOf(3)
        b_arr[0] = 1
        b_arr[1] = -1

        val arr = arrayOf(1, 2, -3, 6, 4, 8, -1)
        val positiveNums = arr.filter { it > 0 }
        val degree2Nums = arr.map { i -> i * i }

        var list = listOf("1", "2", "3")
        val map = mapOf(1 to "one", 2 to "two")
    }
}