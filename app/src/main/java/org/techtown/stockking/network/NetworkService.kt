package org.techtown.stockking.network

import org.techtown.stockking.model.StockDetailModel
import org.techtown.stockking.model.StockTopListModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object NetWorkService{
    private const val BASE_URL ="https://compact-haiku-335913.du.r.appspot.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val api: ApiInterface = retrofit.create(ApiInterface::class.java)
}


interface ApiInterface{
    @GET("{symbol}")
    fun stockTopList1(
        @Path("symbol") arg:String
    ): Call<StockTopListModel>

    @GET("{symbol}")
    fun stockDetail(
            @Path("symbol") arg:String
    ): Call<StockDetailModel>
}