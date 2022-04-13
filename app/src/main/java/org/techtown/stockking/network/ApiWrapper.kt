package org.techtown.stockking.network

import android.util.Log
import android.util.Log.i
import org.techtown.stockking.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiWrapper {
    companion object{

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
        fun getStockDaily(symbol: String, callback: (List<StockModel>) -> Unit){
            val modelCall = NetWorkService.api2.stockDaily(symbol)
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

        fun getCompanyInfo(symbol: String, callback: (List<CompanyInfoModel>) -> Unit){
            val call = NetWorkService.api2.companyInfo(symbol)
            call.enqueue(object : Callback<List<CompanyInfoModel>> {
                override fun onResponse(call: Call<List<CompanyInfoModel>>, response: Response<List<CompanyInfoModel>>) {
                    val list = response.body()
                    i(TAG,"companyinfo response")
                    i(TAG,list.toString())
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

//        fun getImg(symbol: String, callback: (Response<String>) -> Unit){
//            val call = NetWorkService.api2.companyimg()
//            call.enqueue(object : Callback<List<CompanyInfoModel>> {
//                override fun onResponse(call: Call<Response<String>>, response: Response<Response<String>>) {
//                    val list = response.body()
//                    i(TAG,"companyinfo response")
//                    i(TAG,list.toString())
//                    list?.let{
//                        callback.invoke(it)
//                    }
//                }
//                override fun onFailure(call: Call<Response<String>>, t: Throwable) {
//                    i(TAG,"companyinfo fail")
//                    call.cancel()
//                }
//            })
//        }
    fun postToken(userData: UserModel , onResult: (UserModel?)->Unit){
        val modelCall = NetWorkService.api2.requestLogin(userData)
        modelCall.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>
            ) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.i("sss","cancel : "+"t:"+t)
                modelCall.cancel()
            }
        })
    }

    }

}