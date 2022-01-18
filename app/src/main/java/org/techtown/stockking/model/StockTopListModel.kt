package org.techtown.stockking.model

data class StockTopListModel(
    var symbols: List<StockTopList>
)

data class StockTopList(
    var title: String,
    var company: String,
    var price: String,
    var percent: String
)
