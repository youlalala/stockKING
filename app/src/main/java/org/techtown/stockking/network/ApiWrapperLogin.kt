package org.techtown.stockking.network

import android.content.Context
import android.content.Intent
import android.util.Log
import org.techtown.stockking.model.FirstLoginModel
import org.techtown.stockking.model.ResponseLoginModel
import org.techtown.stockking.model.UserModel
import org.techtown.stockking.module.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiWrapperLogin {
    companion object {
        private val TAG = this.javaClass.simpleName
        fun postFirstLogin(
            socialName: String,
            userData: FirstLoginModel,
            callback: (ResponseLoginModel) -> Unit
        ) {
            val modelCall = NetWorkService.api.requestFirstLogin(socialName, userData)
            modelCall.enqueue(object : Callback<ResponseLoginModel> {
                override fun onResponse(
                    call: Call<ResponseLoginModel>, response: Response<ResponseLoginModel>
                ) {
                    Log.i(
                        TAG,
                        "post first login success\n response: " +
                                response
                    )
                    val response = response.body()
                    response?.let {
                        callback.invoke(it)
                    }
                }

                override fun onFailure(call: Call<ResponseLoginModel>, t: Throwable) {
                    Log.i(TAG, "post first login fail : " + t.toString())
                    modelCall.cancel()
                }
            })
        }

        fun getAutoLogin(token: String, callback: (ResponseLoginModel?) -> Unit) {
            val modelCall = NetWorkService.api.requestAutoLogin(token)
            modelCall.enqueue(object : Callback<ResponseLoginModel> {
                override fun onResponse(
                    call: Call<ResponseLoginModel>, response: Response<ResponseLoginModel>
                ) {
                    Log.i(TAG, "get auto login success\n response.body : " + response.body().toString())
                    if(response.isSuccessful){
                        callback(response.body())
                    }else{
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<ResponseLoginModel>, t: Throwable) {
                    Log.i(TAG, "get auto login fail\n error msg:"+t.toString())
                    modelCall.cancel()
                }
            })
        }
    }
}