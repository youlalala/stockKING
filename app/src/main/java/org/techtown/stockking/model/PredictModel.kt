package org.techtown.stockking.model

data class PredictModel(
    var symbol: String,
    var date: String,
    var percent: String,
    var price: String,
    var recommend: String
)