package com.example.googlelogin

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface WebService {
    @GET
    fun myNetworkCall(@Url url: String): Call<ResponseBody>
}
