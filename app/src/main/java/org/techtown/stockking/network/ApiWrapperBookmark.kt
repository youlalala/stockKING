package org.techtown.stockking.network

import android.util.Log
import org.techtown.stockking.model.BookMarkPersonalModel
import org.techtown.stockking.model.BookmarkModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiWrapperBookmark {
    companion object{
        private val TAG = this.javaClass.simpleName
        fun postBookmark(
            token: String,
            bookmarkData: BookmarkModel){
            val modelCall = NetWorkService.api2.requestBookmark(
                authHeader = token,
                bookmarkData)
            modelCall.enqueue(object : Callback<BookmarkModel> {
                override fun onResponse(call: Call<BookmarkModel>, response: Response<BookmarkModel>
                ) {
                    Log.i("sss", "success : " + "response:" + response)
                    //onResult(response.body())
                }
                override fun onFailure(call: Call<BookmarkModel>, t: Throwable) {
                    Log.i("sss","cancel : "+"t:"+t)
                    modelCall.cancel()
                }
            })
        }
        fun getBookmark(token: String, callback: (List<BookMarkPersonalModel>) -> Unit){
            val modelCall = NetWorkService.api2.bookMarkPersonalList(token)
            modelCall.enqueue(object : Callback<List<BookMarkPersonalModel>> {
                override fun onResponse(call: Call<List<BookMarkPersonalModel>>, response: Response<List<BookMarkPersonalModel>>) {
                    Log.i(TAG, "get bookmark success")
                    val data = response.body()
                    data?.let {
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<BookMarkPersonalModel>>, t: Throwable) {
                    Log.i(TAG, "get bookmark fail")
                    modelCall.cancel()
                }
            })
        }
    }
}