package com.example.chatgpt.view

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.chatgpt.data.GPT3Repository
import com.example.chatgpt.databinding.ActivityMainBinding
import com.example.chatgpt.viewmodel.ChatViewModel
import kotlinx.coroutines.launch
import okhttp3.*

class ChatGPTActivity : AppCompatActivity() {
    private lateinit var viewModel: ChatViewModel
    private lateinit var binding: ActivityMainBinding
    private val SPEECH_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        viewModel.state.observe(this, Observer { state ->
            if (state.error != null) {
                Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show()
            }

            if (state.isLoading == true) {

                binding.progressBar.isVisible = true

            } else {
                binding.progressBar.isVisible = false
                binding.textView.text = state.responceText
            }
        })
        binding.submitButton.setOnClickListener {
            val userInput = binding.editText.text.toString()

            lifecycleScope.launch {
                try {
                    viewModel.sendQuery(userInput)
                } catch (e: Exception) {
                    Toast.makeText(this@ChatGPTActivity, "ошибка: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        binding.voiceInputButton.setOnClickListener {
            startSpeechRecognition()
        }
    }


    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите что-нибудь!")

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "Голосовой ввод не поддерживается на вашем устройстве.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (results != null && results.isNotEmpty()) {
                val spokenText = results[0] // Первый результат голосового ввода
                // Теперь вы можете использовать spokenText в вашем приложении
                binding.editText.setText(spokenText)
            }
        }
    }
}








  /*
  OkHttpClient:

Что это: OkHttpClient - это библиотека для выполнения HTTP-запросов и управления сетевым соединением.
Она предоставляет низкоуровневый API для отправки запросов и получения ответов.
Вы можете использовать OkHttpClient для создания собственных HTTP-запросов и работы с сетевыми операциями напрямую.
Когда использовать: OkHttpClient полезен, когда вам нужно полный контроль над HTTP-запросами, или если вы работаете с API,
которое не соответствует стандартам REST и требует более сложных запросов.
Пример использования:

val client = OkHttpClient()
val request = Request.Builder()
    .url("https://example.com/api/endpoint")
    .build()

client.newCall(request).enqueue(object : Callback {
    override fun onFailure(call: Call, e: IOException) {
        // Обработка ошибки
    }

    override fun onResponse(call: Call, response: Response) {
        // Обработка успешного ответа
    }
})

Retrofit:

Что это: Retrofit - это высокоуровневая библиотека, построенная поверх OkHttpClient,
которая упрощает выполнение HTTP-запросов и обработку JSON-ответов.
Она предоставляет абстрактный интерфейс для описания структуры API и автоматическую сериализацию/десериализацию данных.
Retrofit позволяет создавать запросы как методы интерфейса и обрабатывать ответы с помощью POJO-классов.

Пример использования:
val retrofit = Retrofit.Builder()
    .baseUrl("https://example.com/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient()) // Вы можете настроить OkHttpClient здесь
    .build()

val apiService = retrofit.create(ApiService::class.java)

// Определение метода для выполнения запроса
val call = apiService.getData()

call.enqueue(object : Callback<DataResponse> {
    override fun onFailure(call: Call<DataResponse>, t: Throwable) {
        // Обработка ошибки
    }

    override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
        // Обработка успешного ответа
    }
})
Когда использовать: Retrofit удобен, когда у вас есть структурированный RESTful API,
и вы хотите упростить выполнение запросов и обработку данных.
Он также позволяет использовать аннотации для описания запросов и ответов, что делает код более читаемым.


   */

