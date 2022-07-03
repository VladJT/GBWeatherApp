@file:Suppress("unused", "RedundantNullableReturnType", "UNUSED_VARIABLE")

package jt.projects.gbweatherapp.memo

class Lambda {
    var name: String = ""

    fun testFuncInterface() {
        val callback = Testable { val i = 100 }
    }

    fun interface Testable {
        fun test()
    }
}

class Kotlin {
    // анонимная функция
    var funField = fun(s: String): Int {
        if (s.length > 5) return 1
        else return 0
    }

    // лямбда
    // name@ - даем алиас лямбде, для организации возможности нескольких return
    var funFieldL = hacked@{ s: String ->
        if (s.length > 5) return@hacked 1
        else return@hacked 0
    }

    fun math(argFunType: (Int, Int) -> Int, a: Int, b: Int): Unit {
        println(argFunType.invoke(a, b))
    }

    // sum, sum2 - переменные фугкционального типа (ссылаются на функцию)
    val sum = { a: Int, b: Int -> a + b }   // лямбда
    val div = { a: Int, b: Int -> a / b }   // лямбда
    val sum2 = fun(a: Int, b: Int): Int { return a + b } // анонимная функция

    // функция расширение
    fun Lambda.getName(): String {
        return "name = " + this.name// видим поле класса
    }

    // фунция высшего порядка (принимает или возвращает функциональный тип)
    fun highOrderFunction(f: (i: Int) -> Double, c: Char): (_: Int) -> Double {
        return { int: Int -> int.toDouble() }
    }
}