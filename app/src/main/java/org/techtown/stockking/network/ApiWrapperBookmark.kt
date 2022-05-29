package org.techtown.stockking.network

import android.util.Log
import org.techtown.stockking.model.BookmarkModel
import org.techtown.stockking.model.ResponseBookmarkListModel
import org.techtown.stockking.model.ResponseBookmarkModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiWrapperBookmark {
    companion object{
        private val TAG = this.javaClass.simpleName
        fun addFavorite(
            token: String,
            bookmarkData: BookmarkModel){
            val modelCall = NetWorkService.api.addFavorite(
                authHeader = token,
                bookmarkData)
            modelCall.enqueue(object : Callback<ResponseBookmarkModel> {
                override fun onResponse(call: Call<ResponseBookmarkModel>, response: Response<ResponseBookmarkModel>
                ) {
                    Log.i("sss", "success : " + "response:" + response.body())
                    //onResult(response.body())
                }
                override fun onFailure(call: Call<ResponseBookmarkModel>, t: Throwable) {
                    Log.i("sss","cancel : "+"t:"+t)
                    modelCall.cancel()
                }
            })
        }
        fun deleteFavorite(
            token: String,
            bookmarkData: BookmarkModel){
            val modelCall = NetWorkService.api.deleteFavorite(
                authHeader = token,
                bookmarkData)
            modelCall.enqueue(object : Callback<ResponseBookmarkModel> {
                override fun onResponse(call: Call<ResponseBookmarkModel>, response: Response<ResponseBookmarkModel>
                ) {
                    Log.i("sss", "success : " + "response:" + response.body())
                    //onResult(response.body())
                }
                override fun onFailure(call: Call<ResponseBookmarkModel>, t: Throwable) {
                    Log.i("sss","cancel : "+"t:"+t)
                    modelCall.cancel()
                }
            })
        }
        fun getBookmarkList(token: String, callback: (ResponseBookmarkListModel) -> Unit){
            val modelCall = NetWorkService.api.bookMarkPersonalList(token)
            modelCall.enqueue(object : Callback<ResponseBookmarkListModel> {
                override fun onResponse(call: Call<ResponseBookmarkListModel>, response: Response<ResponseBookmarkListModel>) {
                    Log.i(TAG, "get bookmark success response.body: "+response.body())
                    val data = response.body()
                    data?.let {
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<ResponseBookmarkListModel>, t: Throwable) {
                    Log.i(TAG, "get bookmark fail")
                    modelCall.cancel()
                }
            })
        }
    }
}