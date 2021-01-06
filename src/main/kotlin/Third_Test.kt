import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {

    givenCancellableJob_whenRequestForCancel_thenShouldQuit()
    println("Done1")

    try{givenAsyncAction_whenDeclareTimeout_thenShouldFinishWhenTimedOut()}
    catch(e: Exception){
        println(e.message)
    }
    println("Done2")

    givenHaveTwoExpensiveAction_whenExecuteThemAsync_thenTheyShouldRunConcurrently()
    println("Done3")
}

fun givenCancellableJob_whenRequestForCancel_thenShouldQuit() {
    runBlocking<Unit> {
        // given
        val job = launch {
            while (this.isActive) {
                println("is working")
                delay(1L)
            }
        }
        println("Abwarten")
        delay(1300L)
        println("Abgewartet")

        // when
        job.cancel()
        println("Abgebrochen")

        // then cancel successfully

    }
}

fun givenAsyncAction_whenDeclareTimeout_thenShouldFinishWhenTimedOut() {
    runBlocking<Unit> {
        withTimeout(1300L) {
            repeat(1000) { i ->
                println("Some expensive computation $i ...")
                delay(500L)
            }
        }
    }
}

fun givenHaveTwoExpensiveAction_whenExecuteThemAsync_thenTheyShouldRunConcurrently() {
    runBlocking<Unit> {
        val delay = 1000L
        val time = measureTimeMillis {
            // given
            val one = async {
                someExpensiveComputation(delay)
            }
            val two = async {
                someExpensiveComputation(delay)
            }

            // when
            runBlocking {
                one.await()
                two.await()
            }
        }

        // then
        println("time: "+ time + "; delay*2: " + delay*2)
    }
}
suspend fun someExpensiveComputation(delay: Long) {
    delay(delay)
}