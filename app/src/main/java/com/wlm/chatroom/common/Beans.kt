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

data class Discuss(
    val createTime: String,
    val discussId: Int,
    val discussTitle: String,
    val modifyId: String,
    val modifyTime: String,
    val status: String,
    val userId: String,
    val visibleType: String
)