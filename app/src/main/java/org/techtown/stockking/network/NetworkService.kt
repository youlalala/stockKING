package org.techtown.stockking.network

import com.google.gson.GsonBuilder
import org.techtown.stockking.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object NetWorkService{
    private const val BASE_URL =" http://teststock.cafe24app.com"
    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val api: ApiInterface = retrofit.create(ApiInterface::class.java)
}


interface ApiInterface{
    @GET("realtime/{exchange}/desc")
    fun topListRealtime(
        @Path("exchange") arg:String
    ): Call<List<TopListRealtimeModel>>

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

    @GET("stock/company/additional/{symbol}")
    fun companyInfoAdd(
        @Path("symbol") arg:String
    ): Call<List<CompanyInfoAddModel>>

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
    //1년
    @GET("stock/daily/specific/for-5yearly/{symbol}")
    fun stock5Yearly(
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

    @POST("login/{social_name}")
    fun requestFirstLogin(
        @Path("social_name") arg:String,
        @Body userData: FirstLoginModel
    ): Call<ResponseLoginModel>

    @GET("login/auto-login")
    fun requestAutoLogin(
        @Header("authorization") authHeader:String,
    ): Call<ResponseLoginModel>

    @HTTP(method = "DELETE", path = "login", hasBody = true)
    fun deleteAccount(
        @Header("authorization") authHeader:String,
    ): Call<ResponseWithdrawModel>

    @POST("favorite")
    fun addFavorite(
        @Header("authorization") authHeader:String,
        @Body bookmarkData: BookmarkModel
    ): Call<ResponseBookmarkModel>

    @HTTP(method = "DELETE", path = "favorite", hasBody = true)
    fun deleteFavorite(
        @Header("authorization") authHeader:String,
        @Body bookmarkData: BookmarkModel
    ): Call<ResponseBookmarkModel>

    @GET("favorite/all")
    fun bookMarkPersonalList(
        @Header("authorization") authHeader:String,
    ): Call<ResponseBookmarkListModel>
}