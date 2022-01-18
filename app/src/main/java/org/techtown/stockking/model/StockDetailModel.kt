package org.techtown.stockking.model

data class StockDetailModel (
    var tsla: List<StockList>
)

data class StockList(
    var id: Int,
    var date: String,
    var price: String,
    var volume: String
)