package com.example.chatgpt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatgpt.data.GPT3Repository
import com.example.chatgpt.data.model.ChatResponse
import com.example.chatgpt.view.model.ChatGPTState
import okhttp3.OkHttpClient

class ChatViewModel : ViewModel() {
    private val repository = GPT3Repository(client = OkHttpClient())
    private val _state = MutableLiveData<ChatGPTState>(ChatGPTState(false, ""))
    val state: LiveData<ChatGPTState>
        get() = _state

    suspend fun sendQuery(query: String) {

        _state.value = ChatGPTState(true, "")
        val textResponse = repository.sendMessage(query)

        if (textResponse.isSuccess){
            _state.value = ChatGPTState(false, textResponse.getOrNull())
        }else{
            _state.value = ChatGPTState(false, "", textResponse.exceptionOrNull())
        }

    }
}