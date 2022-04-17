package org.techtown.stockking.network

import org.techtown.stockking.model.StockModel
import org.techtown.stockking.model.StockTopListModel
import org.techtown.stockking.model.CompanyInfoModel
import org.techtown.stockking.model.UserModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object NetWorkService{
    //private const val BASE_URL ="https://compact-haiku-335913.du.r.appspot.com/"
    private const val BASE_URL2 ="http://172.30.1.48:8080"
//    private val retrofit: Retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
    private val retrofit2: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL2)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    //val api: ApiInterface = retrofit.create(ApiInterface::class.java)
    val api2: ApiInterface = retrofit2.create(ApiInterface::class.java)
}


interface ApiInterface{
    @GET("{toplist}")
    fun stockTopList(
        @Path("toplist") arg:String
    ): Call<StockTopListModel>

    @GET("company-info/{symbol}")
    fun companyInfo(
        @Path("symbol") arg:String
    ): Call<List<CompanyInfoModel>>

    @GET("intraday/{symbol}")
    fun stockIntraday(
        @Path("symbol") arg1:String,
    ): Call<List<StockModel>>

    @GET("daily/full-data/{symbol}")
    fun stockDaily(
        @Path("symbol") arg1:String,
    ): Call<List<StockModel>>

    @GET("sendimg")
    fun companyimg(): Response<String>

    @POST("login")
    fun requestLogin(
        @Body userData: UserModel
    ): Call<UserModel>
}