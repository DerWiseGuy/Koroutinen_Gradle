import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    println("Start")

// Start a coroutine
    val job = GlobalScope.launch {
        delay(1000)
        println("Hello")
    }

    //Thread.sleep(2000) // wait for 2 seconds
    runBlocking { job.join()}
    println("Stop")

    //1000000 coroutines, much cheaper than Treads
    val c = AtomicLong()

    /*for (i in 1..1_000_000L)
        thread(start = true) {
            c.addAndGet(i)
        }*/
    for (i in 1..1_000_000L)
        GlobalScope.launch {
            c.addAndGet(i)
        }

    println(c.get())

    //Speichere Ergebnisse in deferred zwischen
    //1sek delay in jeder Koroutine, aber dennoch Abschluss nach 10 Sekunden -> Koroutinen laufen alle parallel
    val deferred = (1..1_000_000).map { n ->
        GlobalScope.async {
            workload(n)
        }
    }
    runBlocking{
        val sum = deferred.sumOf { it.await().toLong() }
        println("Sum: $sum")
    }
}

//Funktionen, die asynchronen Code enthalten/evtl länger auf eine Antwort warten llassen müssen mit suspend markiert werden
//nur in solchen Funktionen können weitere Suspend-Funktionen verarbeitet werden
suspend fun workload(n: Int): Int {
    delay(1000)
    return n
}