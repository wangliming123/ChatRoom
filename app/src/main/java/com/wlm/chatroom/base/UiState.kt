package com.wlm.chatroom.base

data class UiState<T>(
    val loading: Boolean,
    val error: String?,
    val success: T?
)