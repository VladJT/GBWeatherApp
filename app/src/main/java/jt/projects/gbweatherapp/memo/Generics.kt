@file:Suppress("unused", "RedundantNullableReturnType", "UNUSED_VARIABLE")

package jt.projects.gbweatherapp.memo

// ДЛЯ СВЕДЕНИЯ

class Test {

    val s = testGen(1) + testGen("3") + testGen(null)

    fun <T> testGen(input: T): String {
        return input.toString()
    }
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