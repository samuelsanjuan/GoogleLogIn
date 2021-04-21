package com.example.googlelogin

import android.content.ContentResolver
import android.telecom.Call
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.*
interface WebService {

    abstract val contentResolver: ContentResolver?
    @GET
    fun getDispositivo(@Url url:String): Call<Dispositivo>

    fun myNetworkCall(@Url url: String): Call<ResponseBody>
}
