package com.wlm.chatroom.base

import com.wlm.chatroom.common.net.HttpResponse


open class BaseRepository {
    suspend fun <T : Any> apiCall(call: suspend () -> HttpResponse<T>): HttpResponse<T> {
        return call.invoke()
    }

//    suspend fun <T: Any> saveApiCall(call: suspend () -> RequestResult<T>, errorMessage: String): RequestResult<T> {
//        return try {
//            call()
//        }catch (e: Exception) {
//            RequestResult.Error(IOException(errorMessage, e))
//        }
//    }
}