import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    val fibonacciSeq = sequence<Int> {
        var a = 0
        var b = 1

        yield(1)

        while (true) {
            yield(a + b)

            val tmp = a + b
            a = b
            b = tmp
        }
    }

    val res = fibonacciSeq
        .take(5)
        .toList()

    println(res)

    givenAsyncCoroutine_whenStartIt_thenShouldExecuteItInTheAsyncWay()
    println("Done1")

    givenHugeAmountOfCoroutines_whenStartIt_thenShouldExecuteItWithoutOutOfMemory()
    println("Done2")
}

suspend fun expensiveComputation(res: MutableList<String>) {
    delay(1000L)
    res.add("word!")
}

fun givenAsyncCoroutine_whenStartIt_thenShouldExecuteItInTheAsyncWay() {
    // given
    val res = mutableListOf<String>()

    // when
    runBlocking<Unit> {
        val promise = launch {
            expensiveComputation(res)
        }
        res.add("Hello,")
        promise.join()
    }

    // then
    res.forEach{println(it)}
}

fun givenHugeAmountOfCoroutines_whenStartIt_thenShouldExecuteItWithoutOutOfMemory() {
    runBlocking<Unit> {
        // given
        val counter = AtomicInteger(0)
        val numberOfCoroutines = 100_000

        // when
        val jobs = List(numberOfCoroutines) {
            launch {
                delay(1000L)
                counter.incrementAndGet()
            }
        }
        jobs.forEach { it.join() }

        // then
        println("Counter: " + counter + "; numberOfCoroutines: " + numberOfCoroutines)
    }
}