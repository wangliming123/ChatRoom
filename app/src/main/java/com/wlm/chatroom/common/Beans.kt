package com.wlm.chatroom.common

data class LoginResult(
    val createTime: String,
    val email: String,
    val id: String,
    val mobile: String,
    val modifyTime: String,
    val nickname: String,
    val password: String,
    val rePassword: String,
    val userName: String
)