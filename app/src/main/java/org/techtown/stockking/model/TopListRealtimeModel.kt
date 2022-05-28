package org.techtown.stockking.model

data class TopListRealtimeModel (
    var change_percent: String,
    var change_value: String,
    var symbol: String,
    var name: String,
    var img:String,
    var close:String,
    var hit: String
)