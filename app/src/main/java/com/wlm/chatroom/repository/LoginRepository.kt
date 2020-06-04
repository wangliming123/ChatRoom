package com.wlm.chatroom.repository

import com.wlm.chatroom.base.BaseRepository
import com.wlm.chatroom.common.LoginResult
import com.wlm.chatroom.common.net.HttpResponse
import com.wlm.chatroom.common.net.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository : BaseRepository(){

    suspend fun login(username: String, password: String): HttpResponse<LoginResult> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.login(username, password)
        }
    }
//
    suspend fun register(username: String, password: String, rePassword: String): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.register(username, password, rePassword)
        }
    }

    suspend fun logout(): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.logout()
        }
    }

    suspend fun editUser(editMap: Map<String, String>): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.editUser(editMap)
        }
    }
}