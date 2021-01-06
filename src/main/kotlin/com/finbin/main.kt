import com.finbin.AccumulateDaily
import com.finbin.StockQuote
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.vhl.blackmo.grass.dsl.grass

@ExperimentalStdlibApi
fun main() {
    println("Hello Kotlin/Native!")

    val file = StockQuote::class.java.getResource("/ETH-USD.csv").readText()
    val csvContents = csvReader().readAllWithHeader(file)
    val data = grass<StockQuote>().harvest(csvContents)
    val btcProcessor = AccumulateDaily(30, "Thingy", 10f, 0.04f, 1.50f)
    data.forEach {
        record -> btcProcessor.evaluate(record)
    }
    btcProcessor.complete()
    println("GOT:" + data.size)

}