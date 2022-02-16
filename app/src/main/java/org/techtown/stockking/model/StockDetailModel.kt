package org.techtown.stockking.model

data class StockDetailModel (
    var currentPage: String,
    var symbolStart: String,
    var contents: List<StockList>
)

data class StockList(
    var name: String,
    var timestamp: String,
    var open: String,
    var high: String,
    var low: String,
    var close: String,
    var volume: String
)