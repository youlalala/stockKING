package org.techtown.stockking.model


data class CompanyInfoModel (
    var symbol: String,
    var name_en: String,
    var name_kr: String,
    var desc_en: String,
    var desc_kr: String,
    var img: String,
    var shareout: String,
    var close: String,
    var change_percent: String,
    var change_value: String
)
