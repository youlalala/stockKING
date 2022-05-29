package org.techtown.stockking.model

data class ResponseBookmarkListModel(
    var isSuccess: String,
    var code: String,
    var message: String,
    var result: BookmarkListResult
)

data class BookmarkListResult(
    var user_id: String,
    var symbol: List<BookmarkListModel>
)

data class BookmarkListModel(
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