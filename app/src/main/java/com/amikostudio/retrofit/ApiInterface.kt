package com.amikostudio.retrofit


import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {


    @POST("register")
    fun register(@Body map: HashMap<String, Any>): Call<RegisterResponse>

    @POST("otp_verify")
    fun otp(@Body map: HashMap<String, Any>): Call<RegisterResponse>

    @POST("login")
    fun login(@Body map: HashMap<String, Any>): Call<RegisterResponse>

    @GET("anime_list")
    fun AnimeList() : Call<AminoListResponse>

    @POST("anime_by_id")
    fun AnimeDetail(@Body map: HashMap<String, Any>): Call<AmiakoDetailResponse>

    @POST("saved_to_watch_later")
    fun saved_to_watch_later(@Body map: HashMap<String, Any>): Call<Common>

    @POST("save_anime")
    fun saved_to_save(@Body map: HashMap<String, Any>): Call<Common>

    @POST("saved_to_favourite")
    fun saved_to_favouriate(@Body map: HashMap<String, Any>): Call<Common>

    @GET("get_saved_to_watch_later")
    fun getWatchLater() : Call<WatchLaterListResponse>

    @GET("get_saved_anime")
    fun save() : Call<WatchLaterListResponse>

    @GET("get_from_favourite")
    fun favouriate() : Call<WatchLaterListResponse>

    @POST("change_password")
    fun changePassword(@Body map: HashMap<String, Any>): Call<Common>

    @GET("get_profile")
    fun getProfile() : Call<getProfileResponse>



    @Multipart
    @POST("edit_profile")
    fun edit_profile(
        @Part("user_name") name: RequestBody,
        @Part image: MultipartBody.Part?): Call<RegisterResponse>







}