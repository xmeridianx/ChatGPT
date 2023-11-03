package com.example.chatgpt.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class GPT3Repository(val client: OkHttpClient) {
    val apiKey = "notforyoubro"
    val url = "https://api.openai.com/v1/engines/text-davinci-003/completions"

    suspend fun sendMessage(text: String): Result<String>{
        return withContext(Dispatchers.IO){
            try {
                val requestBody = """
            {
            "prompt": "$text",
            "max_tokens": 500,
            "temperature": 0
            }
        """.trimIndent()
                val request = Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
                    .build()

                val body = client.newCall(request).execute().body?.string()

                if (body != null) {
                    val jsonObject = JSONObject(body)
                    val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                    val textResult = jsonArray.getJSONObject(0).getString("text")
                    return@withContext Result.success(textResult)

                } else {
                    return@withContext Result.failure(Throwable("Errorrrr"))

                }
            }catch (e: Throwable){
                return@withContext Result.failure(e)
            }
        }

    }

}
