package org.techtown.stockking.model

data class ResponseLoginModel(
    var isSuccess: String,
    var code: String,
    var message: String,
    var result: UserModel
)

data class UserModel (
    var userId: String,
    var name: String,
    var userToken: String,
)