package org.techtown.stockking.network

import android.util.Log
import org.techtown.stockking.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiWrapper {

    companion object{
        fun getStockTopList(callback: (List<StockTopList>) -> Unit){
            val modelCall = NetWorkService.api.stockTopList1("symbol")
            modelCall.enqueue(object : Callback<StockTopListModel> {
                override fun onResponse(call: Call<StockTopListModel>, response: Response<StockTopListModel>) {
                    val list = response.body()
                    Log.i("SSS", "response")
                    list?.let{
                        callback.invoke(it.symbols)
                    }
                }
                override fun onFailure(call: Call<StockTopListModel>, t: Throwable) {
                    Log.i("SSS", "failure")
                    modelCall.cancel()
                }
            })
        }

        fun getStockDetail(symbol: String, callback: (List<StockList>) -> Unit){
            val modelCall = NetWorkService.api2.stockDetail(symbol,"1","10")
            modelCall.enqueue(object : Callback<StockDetailModel> {
                override fun onResponse(call: Call<StockDetailModel>, response: Response<StockDetailModel>) {
                    val list = response.body()
                    list?.let {
                        Log.i("SSS",it.toString())
                        callback.invoke(it.contents)

                    }
                }
                override fun onFailure(call: Call<StockDetailModel>, t: Throwable) {
                    modelCall.cancel()

                }
            })

        }
    }

}