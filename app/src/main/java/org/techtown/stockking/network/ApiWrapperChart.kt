package org.techtown.stockking.network

import android.util.Log
import org.techtown.stockking.model.StockModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiWrapperChart {
    companion object{
        private val TAG = this.javaClass.simpleName
        fun getStockDaily(symbol: String, callback: (List<StockModel>) -> Unit){
            val modelCall = NetWorkService.api.stockDaily(symbol)
            modelCall.enqueue(object : Callback<List<StockModel>> {
                override fun onResponse(call: Call<List<StockModel>>, response: Response<List<StockModel>>) {
                    val data = response.body()
                    data?.let {
                        Log.i(TAG, "daily stock response : " + data.toString())
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    Log.i(TAG, "daily stock fail : "+ t.toString())
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
                        Log.i(TAG, "weekly stock response")
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    Log.i(TAG, "weekly stock fail")
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
                        Log.i(TAG, "1monthly stock response")
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    Log.i(TAG, "1monthly stock fail")
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
                        Log.i(TAG, "3monthly stock response")
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    Log.i(TAG, "3monthly stock fail")
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
                        Log.i(TAG, "Yearly stock response")
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    Log.i(TAG, "Yearly stock fail")
                    modelCall.cancel()
                }
            })
        }

        fun getStock5Yearly(symbol: String, callback: (List<StockModel>) -> Unit){
            val modelCall = NetWorkService.api.stock5Yearly(symbol)
            modelCall.enqueue(object : Callback<List<StockModel>> {
                override fun onResponse(call: Call<List<StockModel>>, response: Response<List<StockModel>>) {
                    val data = response.body()
                    data?.let {
                        Log.i(TAG, "5Yearly stock response")
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<StockModel>>, t: Throwable) {
                    Log.i(TAG, "5Yearly stock fail")
                    modelCall.cancel()
                }
            })
        }
    }
}