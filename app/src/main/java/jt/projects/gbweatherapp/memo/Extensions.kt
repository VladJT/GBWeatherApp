@file:Suppress("unused", "RedundantNullableReturnType", "UNUSED_VARIABLE")

package jt.projects.gbweatherapp.memo

class Extensions {

    class City(val name: String) {
        var population: Int = 0
        fun solveOptimalSquare() {}
    }

    // Функция let обычно используется для проверки на null:
    // personId?.let { id ->/loadPerson(id)}
    //
    // Объект "test" внутри блока доступен как it
    //Возвращает результат выполнения lambda-функции
    val length = "test".let {
        println(it)
        it.length
    }


    //Also полезен для выполнения дополнительных действий над объектом (не его изменений) и
    //валидации, прежде чем присваивать значение переменной:
    //val author = author.also {
    //    requireNotNull(it.age)
    //    print(it.name)  }
    //
    //Объект "test" внутри блока доступен как it
    //Возвращает контекстный объект ("test")
    val test = "test".also {
        println(it)
    }


    // Распространённый сценарий использования этой функции — своеобразная замена паттерна «Строитель/Builder».
    // После вызова конструктора и перед возвратом готового объекта производится его конфигурация:
    // val peter = Person().apply {
    // name = "Peter"
    // age = 18}
    //Объект City("Moscow") внутри блока доступен как this (поэтому для поля popultaion - мы можем опустить обращения и будет population=15_000_000)
    //Возвращает контекстный объект (изменённый City("Moscow"))
    val moscow = City("Moscow").apply {
        this.population = 15_000_000
        println(this)
    }

    // Run применяется, если требуется просчитать какие-то значения или ограничить применение локальных переменных
    //
    // run (с контекстным объектом)
    // Объект City("Moscow") внутри блока доступен как this (поэтому для поля popultaion - мы можем опустить обращения и будет population=15_000_000)
    //  Возвращает результат выполнения lambda-функции (solveOptimalSquare())
    val optimalSquare = City("Moscow").run {
        val p = 15_000_000
        this.population = p
        this.solveOptimalSquare()
    }

    // run (без контекстного объекта)
    //     Нет объекта на котором применятся
    //    Возвращает результат выполнения lambda-функции (test.length)
    val length2 = run {
        val test = "test"
        test.length
    }


    // with - не функция расширения!!!
    // Важно! Используем with только на ресиверах non-nullable, и когда не требуется результат выполнения функции
    // Объект "test" внутри блока доступен как this
    // Возвращает результат выполнения lambda-функции (this.length)
    val length3 = with("test") {
        this.length
    }


}