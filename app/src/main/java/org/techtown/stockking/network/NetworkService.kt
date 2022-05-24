package org.techtown.stockking.network

import org.techtown.stockking.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object NetWorkService{
    private const val BASE_URL =" http://teststock.cafe24app.com"
    private const val BASE_URL2 ="http://172.30.1.37:8080"
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
    @GET("{toplist}")
    fun topListRealtime(
        @Path("toplist") arg:String
    ): Call<TopListRealtimeModel>

    @GET("change/top100/{sort}")
    fun topListChange(
        @Path("sort") arg:String
    ): Call<List<TopListChangeModel>>

    @GET("cap/{exchange}/top100")
    fun topListCap(
        @Path("exchange") arg:String
    ): Call<List<TopListCapModel>>

    @GET("stock/company/specific/{symbol}")
    fun companyInfo(
        @Path("symbol") arg:String
    ): Call<List<CompanyInfoModel>>

    // 1일
    @GET("stock/intraday/specific/for-daily/{symbol}")
    fun stockDaily(
        @Path("symbol") arg1: String,
    ): Call<List<StockModel>>
    // 1주
    @GET("stock/intraday/specific/for-weekly/{symbol}")
    fun stockWeekly(
        @Path("symbol") arg1:String,
    ): Call<List<StockModel>>
    // 1달
    @GET("stock/daily/specific/for-monthly/{symbol}")
    fun stockMonthly(
        @Path("symbol") arg1:String,
    ): Call<List<StockModel>>
    //3달
    @GET("stock/daily/specific/for-3monthly/{symbol}")
    fun stock3Monthly(
        @Path("symbol") arg1:String,
    ): Call<List<StockModel>>
    //1년
    @GET("stock/daily/specific/for-yearly/{symbol}")
    fun stockYearly(
        @Path("symbol") arg1:String,
    ): Call<List<StockModel>>

    //search
    @GET("search/{method}/{keyword}")
    fun search(
        @Path("method") arg1:String,
        @Path("keyword") arg2:String
    ): Call<List<SearchModel>>

    @GET("stock/company/full-data")
    fun allCompany(
    ): Call<List<SearchModel>>

    @POST("login")
    fun requestLogin(
        @Body userData: UserModel
    ): Call<UserModel>

    @POST("bookmark")
    fun requestBookmark(
        @Body bookmarkData: BookmarkModel
    ): Call<BookmarkModel>

    @GET("bookmarkpersonal/{token}")
    fun bookMarkPersonalList(
        @Path("token") arg1:String,
    ): Call<List<BookMarkPersonalModel>>
}