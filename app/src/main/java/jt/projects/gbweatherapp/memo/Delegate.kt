@file:Suppress("unused", "RedundantNullableReturnType", "UNUSED_VARIABLE")
package jt.projects.gbweatherapp.memo

class Delegate {
}


fun main(){
    val lazyMan = LazyMan(Worker1(), Worker2())
    lazyMan.work1() // выполнит worker1
    lazyMan.work2() // выполнит worker2

    val lazyMan2 = LazyMan2()
    lazyMan2.work1()
}

interface Workable1{
    fun work1()
}

interface Workable2{
    fun work2()
}

class Worker1:Workable1{
    override fun work1() {

    }
}

class Worker2:Workable2{
    override fun work2() {

    }
}

class LazyMan(worker1:Worker1, worker2: Worker2) : Workable1 by worker1, Workable2 by worker2{

}

class LazyMan2 : Workable1 by Worker1(), Workable2 by Worker2(){

}