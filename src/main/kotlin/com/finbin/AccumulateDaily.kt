package com.finbin

import com.finbin.Processors.Processor
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class AccumulateDaily(val sinceDays: Long, val name: String, val dailyPurchase:Float, val feePercent: Float, val feeFlat: Float) : Processor(dailyPurchase, feePercent) {

    var newerThan: LocalDate = getAge(sinceDays)

    private fun getAge(sinceDays: Long): LocalDate {
        val fromTime = System.currentTimeMillis() - (sinceDays * 1000 * 60 * 60 * 24)
        return Instant.ofEpochMilli(fromTime).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    var count: Int = 0
    var sum: Double = 0.0
    var sumUp: Double = 0.0
    var sumDown: Double = 0.0
    var balance: Double = 0.0
    var lastQuote: StockQuote = StockQuote(LocalDate.MIN, 0f, 0f, 0f, 0f, 0f, 0)
    var unspentCapital: Float = 0.0f
    var totalFees: Float = 0.0f;

    override fun evaluate(quote: StockQuote) {
        if (quote.Date.compareTo(newerThan) < 0) {
            return
        }
        count++
        sum += quote.High
        // is first entry
        if (lastQuote.Volume.equals(0)) {
            lastQuote = quote
        } else {

            if (buyNow(quote.AdjClose, lastQuote.AdjClose)) {
                sumUp += quote.High - lastQuote.High
                balance += purchase(dailyPurchase, feePercent, quote.AdjClose)
            } else {
                unspentCapital += dailyPurchase
//            } else {
//                sumDown += lastQuote.High - quote.High
            }
        }
            lastQuote = quote
        }

    // fee is percentage of spend - normally 0.02 (2 pc)
    // 1 Unit cost $123 - $100 buys = 100/123 units
    private fun purchase(dailyPurchase: Float, feePercent: Float, quoteAdjClose: Float): Float {
        var feeAmount = dailyPurchase * feePercent
        // charged max of flatFee or feePercentage
        if (feeAmount < feeFlat) {
            feeAmount = feeFlat
        }
        totalFees += feeAmount;
        val remainingPurchaseCapital = dailyPurchase - feeAmount;
        return remainingPurchaseCapital/quoteAdjClose
    }

    override fun complete() {
        println(name + "\n Days:" + count +
                "\n Price: $%,.2f".format(lastQuote.AdjClose) +
                "\n Spend: $%,.2f".format(count * dailyPurchase) +
                "\n Fees: " + totalFees +
                "\n Units: $%,.2f".format(balance) +
                "\n Value: $%,.2f".format(lastQuote.AdjClose * balance)  +
                "\n UnspentCapital: $%,.2f".format(unspentCapital))
    }

    private fun buyNow(adjClose: Float, lastAdjClose1: Float): Boolean {
        return adjClose > lastAdjClose1;
//                return true;// adjClose > lastAdjClose1;
    }
}