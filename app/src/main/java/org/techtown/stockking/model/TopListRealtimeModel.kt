package org.techtown.stockking.model

data class TopListRealtimeModel(
    var symbols: List<StockTopList>
)

data class StockTopList(
    var title: String,
    var company: String,
    var price: String,
    var percent: String,
    //***
    //var img:String
)
