package com.finbin

import java.time.LocalDate
import java.util.*

//Date,Open,High,Low,Close,Adj Close,Volume
//2016-01-04,
// 430.061005,434.516998,429.084015,433.091003,
// 433.091003,38477500
data class StockQuote(
    val Date: LocalDate,
    val Open: Float,
    val High: Float,
    val Low: Float,
    val Close: Float,
    val AdjClose: Float,
    val Volume: Long
) {
    fun max(): Float {
        TODO("Not yet implemented")
    }
}
