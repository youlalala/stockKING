package org.techtown.stockking.network


import android.util.Log.i
import org.techtown.stockking.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        fun getCompanyInfoAdd(symbol: String, callback: (List<CompanyInfoAddModel>) -> Unit){
            val call = NetWorkService.api.companyInfoAdd(symbol)
            call.enqueue(object : Callback<List<CompanyInfoAddModel>> {
                override fun onResponse(call: Call<List<CompanyInfoAddModel>>, response: Response<List<CompanyInfoAddModel>>) {
                    val list = response.body()
                    i(TAG,"companyinfo response")
                    list?.let{
                        callback.invoke(it)
                    }
                }
                override fun onFailure(call: Call<List<CompanyInfoAddModel>>, t: Throwable) {
                    i(TAG,"companyinfo fail")
                    call.cancel()
                }
            })
        }
    }

}