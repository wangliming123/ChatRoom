package com.wlm.chatroom.common

import androidx.room.Entity
import androidx.room.PrimaryKey

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

@Entity
data class Message(
    @PrimaryKey
    val msgId: Int,
    val userId: String,
    val discussId: Int,
    val manId: Int,
    val msgContent: String,
    val msgType: Int,
    val nickname: String,
    val status: String,
    val modifyTime: String,
    val createTime: String
)

data class User(
    val createTime: String,
    val email: String,
    val id: String,
    val mobile: String,
    val modifyTime: String,
    val nickname: String,
    val password: String,
    val rePassword: String,
    val userName: String,
    var checked: Boolean = false
)
data class DiscussMan(
    val createTime: String,
    val discussId: Int,
    val manId: Int,
    val manType: String,
    val modifyTime: String,
    val remind: String,
    val status: String,
    val userId: String
)