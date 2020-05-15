package com.wlm.chatroom.common.net

/**
 * 网络请求通用response
 */
data class HttpResponse<T>(val errorCode: Int, val errorMsg: String?, val data: T?)