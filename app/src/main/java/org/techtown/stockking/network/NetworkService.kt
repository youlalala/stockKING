package org.techtown.stockking.network

import org.techtown.stockking.model.StockDetailModel
import org.techtown.stockking.model.StockTopListModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object NetWorkService{
    private const val BASE_URL ="https://compact-haiku-335913.du.r.appspot.com/"
    private const val BASE_URL2 ="http://172.30.1.48:8080/"
    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val retrofit2: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL2)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiInterface = retrofit.create(ApiInterface::class.java)
    val api2: ApiInterface = retrofit2.create(ApiInterface::class.java)
}


interface ApiInterface{
    @GET("{symbol}")
    fun stockTopList1(
        @Path("symbol") arg:String
    ): Call<StockTopListModel>

    @GET("searchData/list/{symbol}")
    fun stockDetail(
        @Path("symbol") arg1:String,
        @Query("offset") arg2: String,
        @Query("limit") arg3: String
    ): Call<StockDetailModel>
}