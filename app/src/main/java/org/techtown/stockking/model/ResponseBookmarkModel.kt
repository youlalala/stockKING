package org.techtown.stockking.model

data class ResponseBookmarkModel(
    var isSuccess: String,
    var code: String,
    var message: String,
    var result: BookmarkResult
)

data class BookmarkResult(
    var user_id: String,
    var symbol: String
)

