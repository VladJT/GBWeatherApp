package jt.projects.gbweatherapp.utils

// ДЛЯ СВЕДЕНИЯ

class Test {
    fun testNullable() {
        val s: String? = "f"
        val rez: String = s ?: ""
        val len: Int = s?.length ?: 0
    }

    fun testCollections() {
        val b_arr = arrayOf(3)
        b_arr[0] = 1
        b_arr[1] = -1

        val arr = arrayOf(1, 2, -3, 6, 4, 8, -1)
        val positiveNums = arr.filter { it > 0 }
        val degree2Nums = arr.map { i -> i * i }

        var list = listOf("1", "2", "3")
        val map = mapOf(1 to "one", 2 to "two")

        val s = testGen(1) + testGen("3") + testGen(null)
    }

    fun testFuncInterface() {
        val callback = object : Testable {
            override fun test() {
                val i = 100
            }

        }
    }

    fun <T> testGen(input: T): String {
        return input.toString()
    }
}

interface Testable {
    fun test()
}

class ClassGen<T : Number>(x: T) {
    var value: T = x
}

class ClassGenOut<out T>(val x: T) {
    fun getData(): T {
        return x
    }
}

class ClassGenIn<in T>() {
    fun getData(value: T) {

    }
}