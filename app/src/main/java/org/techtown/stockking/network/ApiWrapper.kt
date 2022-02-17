package org.techtown.stockking.network

import android.util.Log
import android.util.Log.i
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.techtown.stockking.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiWrapper {
    companion object{
        //?
        private val TAG = this.javaClass.simpleName
        fun getStockTopList(callback: (List<StockTopList>) -> Unit){
            val modelCall = NetWorkService.api2.stockTopList("realtime")
            modelCall.enqueue(object : Callback<StockTopListModel> {
                override fun onResponse(call: Call<StockTopListModel>, response: Response<StockTopListModel>) {
                    val list = response.body()
                    i(TAG,"stock top list response")
                    list?.let{
                        callback.invoke(it.symbols)
                    }
                }
                override fun onFailure(call: Call<StockTopListModel>, t: Throwable) {
                    i(TAG,"stock top list response")
                    modelCall.cancel()
                }
            })
        }

        //callback!???
        fun getStockIntraday(symbol: String, callback: (List<StockModel>) -> Unit){
            val modelCall = NetWorkService.api2.stockIntraday(symbol)
            modelCall.enqueue(object : Callback<List<StockModel>> {
                override fun onResponse(call: Call<List<StockModel>>, response: Response<List<StockModel>>) {
                    val data = response.body()
                    data?.let {
                        i(TAG,"intraday stock response")
                        callback.invoke(it)

                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    i(TAG,"intraday stock fail")
                    modelCall.cancel()

                }
            })

        }

        fun getCompanyInfo(symbol: String, callback: (List<companyInfoModel>) -> Unit){
            val call = NetWorkService.api2.companyInfo(symbol)
            call.enqueue(object : Callback<List<companyInfoModel>> {
                override fun onResponse(call: Call<List<companyInfoModel>>, response: Response<List<companyInfoModel>>) {
                    val list = response.body()
                    i(TAG,"companyinfo response")
                    i(TAG,list.toString())
                    list?.let{
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<companyInfoModel>>, t: Throwable) {
                    i(TAG,"companyinfo fail")
                    call.cancel()
                }
            })
        }

        fun getStockDetail(symbol: String, callback: (List<StockDetailList>) -> Unit){
            val modelCall = NetWorkService.api2.stockDetail(symbol,10,10)
            modelCall.enqueue(object : Callback<StockDetailModel> {
                override fun onResponse(call: Call<StockDetailModel>, response: Response<StockDetailModel>) {
                    val list = response.body()
                    list?.let {
                        i(TAG,"daily stock response")
                        callback.invoke(it.contents)

                    }
                }
                override fun onFailure(call: Call<StockDetailModel>, t: Throwable) {
                    i(TAG,"daily stock fail")
                    modelCall.cancel()

                }
            })

        }
    }

}