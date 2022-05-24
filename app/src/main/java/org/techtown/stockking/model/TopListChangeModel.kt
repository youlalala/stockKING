package org.techtown.stockking.model

data class TopListChangeModel (
    var change_percent: String,
    var change_value: String,
    var symbol: String,
    var name: String
)