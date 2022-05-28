package org.techtown.stockking.network

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Log.i
import androidx.core.content.ContextCompat.startActivity
import org.techtown.stockking.MainActivity
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.model.*
import org.techtown.stockking.module.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class ApiWrapper {
    companion object{
        private val TAG = this.javaClass.simpleName
        fun getTopListRealtime(exchange: String, callback: (List<TopListRealtimeModel>) -> Unit){
            val modelCall = NetWorkService.api.topListRealtime(exchange)
            modelCall.enqueue(object : Callback<List<TopListRealtimeModel>> {
                override fun onResponse(call: Call<List<TopListRealtimeModel>>, response: Response<List<TopListRealtimeModel>>) {
                    val list = response.body()
                    i(TAG,"stock top list response")
                    list?.let{
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<TopListRealtimeModel>>, t: Throwable) {
                    i(TAG,"stock top list response")
                    modelCall.cancel()
                }
            })
        }

        fun getTopListChange(sort: String, callback: (List<TopListChangeModel>) -> Unit){
            val modelCall = NetWorkService.api.topListChange(sort)
            modelCall.enqueue(object : Callback<List<TopListChangeModel>> {
                override fun onResponse(call: Call<List<TopListChangeModel>>, response: Response<List<TopListChangeModel>>) {
                    val list = response.body()
                    i(TAG,"change top list response")
                    list?.let{
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<TopListChangeModel>>, t: Throwable) {
                    i(TAG,"change top list fail")
                    modelCall.cancel()
                }
            })
        }

        fun getTopListCap(exchange: String, callback: (List<TopListCapModel>) -> Unit){
            val modelCall = NetWorkService.api.topListCap(exchange)
            modelCall.enqueue(object : Callback<List<TopListCapModel>> {
                override fun onResponse(call: Call<List<TopListCapModel>>, response: Response<List<TopListCapModel>>) {
                    val list = response.body()
                    i(TAG,"cap top list response")
                    list?.let{
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<TopListCapModel>>, t: Throwable) {
                    i(TAG,"cap top list fail")
                    modelCall.cancel()
                }
            })
        }

        fun getSearch(method: String, keyword: String, callback: (List<SearchModel>) -> Unit){
            val modelCall = NetWorkService.api.search(method,keyword)
            modelCall.enqueue(object : Callback<List<SearchModel>> {
                override fun onResponse(call: Call<List<SearchModel>>, response: Response<List<SearchModel>>) {
                    val list = response.body()
                    i(TAG,"search list response")
                    list?.let{
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<SearchModel>>, t: Throwable) {
                    i(TAG,"search list fail")
                    modelCall.cancel()
                }
            })
        }

        fun getAllCompany(callback: (List<SearchModel>) -> Unit){
            val modelCall = NetWorkService.api.allCompany()
            modelCall.enqueue(object : Callback<List<SearchModel>> {
                override fun onResponse(call: Call<List<SearchModel>>, response: Response<List<SearchModel>>) {
                    val list = response.body()
                    i(TAG,"all company list response")
                    list?.let{
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<SearchModel>>, t: Throwable) {
                    i(TAG,"all company list  fail")
                    modelCall.cancel()
                }
            })
        }


        fun getStockDaily(symbol: String, callback: (List<StockModel>) -> Unit){
            val modelCall = NetWorkService.api.stockDaily(symbol)
            modelCall.enqueue(object : Callback<List<StockModel>> {
                override fun onResponse(call: Call<List<StockModel>>, response: Response<List<StockModel>>) {
                    val data = response.body()
                    data?.let {
                        i(TAG,"daily stock response")
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    i(TAG,"daily stock fail")
                    modelCall.cancel()
                }
            })
        }

        fun getStockWeekly(symbol: String, callback: (List<StockModel>) -> Unit){
            val modelCall = NetWorkService.api.stockWeekly(symbol)
            modelCall.enqueue(object : Callback<List<StockModel>> {
                override fun onResponse(call: Call<List<StockModel>>, response: Response<List<StockModel>>) {
                    val data = response.body()
                    data?.let {
                        i(TAG,"weekly stock response")
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    i(TAG,"weekly stock fail")
                    modelCall.cancel()
                }
            })
        }

        fun getStockMonthly(symbol: String, callback: (List<StockModel>) -> Unit){
            val modelCall = NetWorkService.api.stockMonthly(symbol)
            modelCall.enqueue(object : Callback<List<StockModel>> {
                override fun onResponse(call: Call<List<StockModel>>, response: Response<List<StockModel>>) {
                    val data = response.body()
                    data?.let {
                        i(TAG,"1monthly stock response")
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    i(TAG,"1monthly stock fail")
                    modelCall.cancel()
                }
            })
        }

        fun getStock3Monthly(symbol: String, callback: (List<StockModel>) -> Unit){
            val modelCall = NetWorkService.api.stock3Monthly(symbol)
            modelCall.enqueue(object : Callback<List<StockModel>> {
                override fun onResponse(call: Call<List<StockModel>>, response: Response<List<StockModel>>) {
                    val data = response.body()
                    data?.let {
                        i(TAG,"3monthly stock response")
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    i(TAG,"3monthly stock fail")
                    modelCall.cancel()
                }
            })
        }

        fun getStockYearly(symbol: String, callback: (List<StockModel>) -> Unit){
            val modelCall = NetWorkService.api.stockYearly(symbol)
            modelCall.enqueue(object : Callback<List<StockModel>> {
                override fun onResponse(call: Call<List<StockModel>>, response: Response<List<StockModel>>) {
                    val data = response.body()
                    data?.let {
                        i(TAG,"3monthly stock response")
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    i(TAG,"3monthly stock fail")
                    modelCall.cancel()
                }
            })
        }


        fun getCompanyInfo(symbol: String, callback: (List<CompanyInfoModel>) -> Unit){
            val call = NetWorkService.api.companyInfo(symbol)
            call.enqueue(object : Callback<List<CompanyInfoModel>> {
                override fun onResponse(call: Call<List<CompanyInfoModel>>, response: Response<List<CompanyInfoModel>>) {
                    val list = response.body()
                    i(TAG,"companyinfo response")
                    list?.let{
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<CompanyInfoModel>>, t: Throwable) {
                    i(TAG,"companyinfo fail")
                    call.cancel()
                }
            })
        }


        fun postBookmark(token: String, bookmarkData: BookmarkModel , onResult: (BookmarkModel?)->Unit){
            val modelCall = NetWorkService.api2.requestBookmark(
                authHeader = token,
                bookmarkData)
            modelCall.enqueue(object : Callback<BookmarkModel> {
                override fun onResponse(call: Call<BookmarkModel>, response: Response<BookmarkModel>
                ) {
                    i("sss","success : "+"response:"+response)
                    onResult(response.body())
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
                    val data = response.body()
                    data?.let {
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<BookMarkPersonalModel>>, t: Throwable) {
                    i(TAG,"intraday stock fail")
                    modelCall.cancel()
                }
            })
        }



    }

}