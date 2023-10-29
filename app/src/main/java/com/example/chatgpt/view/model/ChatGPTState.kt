package com.example.chatgpt.view.model

data class ChatGPTState (
    val isLoading: Boolean,
    val responceText: String?,
    val error: Throwable? = null
)