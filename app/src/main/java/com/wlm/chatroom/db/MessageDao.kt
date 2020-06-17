package com.wlm.chatroom.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wlm.chatroom.common.Message

@Dao
interface MessageDao {
    @Query("select * from Message where discussId == :discussId order by createTime collate nocase asc")
    fun getAllMessage(discussId: Int): DataSource.Factory<Int, Message>

    @Insert
    fun insert(messageList: List<Message>)

    @Query("select * from Message where discussId == :discussId order by createTime collate nocase asc")
    fun getMessageList(discussId: Int): List<Message>
}