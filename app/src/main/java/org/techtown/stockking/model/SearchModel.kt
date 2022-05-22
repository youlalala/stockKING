package org.techtown.stockking.model

data class SearchModel (
    var symbol: String,
    var name_en: String = "",
    var name_kr: String = "",
    var img: String=""
)
