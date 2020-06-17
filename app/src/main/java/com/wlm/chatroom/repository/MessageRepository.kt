package com.wlm.chatroom.repository

import com.wlm.chatroom.MyApp
import com.wlm.chatroom.base.BaseRepository
import com.wlm.chatroom.common.DiscussMan
import com.wlm.chatroom.common.Message
import com.wlm.chatroom.common.net.HttpResponse
import com.wlm.chatroom.common.net.RetrofitManager
import com.wlm.chatroom.viewModel.MessageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageRepository(private val messageViewModel: MessageViewModel) : BaseRepository() {



    suspend fun sendMsg(msg: String, discussId: Int): HttpResponse<Any> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.sendMsg(
                MyApp.instance.currentUserId,
                discussId,
                msg
            )
        }
    }

    suspend fun getMsgList(discussId: Int): HttpResponse<List<Message>> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getMsgList(discussId)
        }
    }

    suspend fun addDiscussMan(discussId: Int, userId: String): HttpResponse<Any> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.addDiscussMan(discussId, userId)
        }
    }

    suspend fun getManList(discussId: Int): HttpResponse<DiscussMan> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getManList(discussId)
        }
    }

}