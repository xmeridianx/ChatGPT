package com.example.chatgpt.data

import com.example.chatgpt.data.model.ChatResponse
import okhttp3.Request
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Gpt3ApiService {
    @POST("completions")
    fun getGpt3Response(@Body request: Request): Call<ChatResponse>
}