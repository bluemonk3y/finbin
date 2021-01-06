package com.finbin

class Processors {

    /**
     * Base class
     */
    open class Processor(dailyPurchase: Float, feePercent: Float) {
        open fun evaluate(quote: StockQuote) {
//            return document.replace(matches, "matched")
        }
        open fun complete() {
            println("DONE!")
        }
    }
}