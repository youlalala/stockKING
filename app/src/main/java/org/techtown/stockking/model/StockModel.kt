package org.techtown.stockking.model

data class StockModel (
    var symbol: String,
    var datetime: String = "",
    var date: String = "",
    var open: String,
    var high: String,
    var low: String,
    var close: String,
    var volume: String
)
