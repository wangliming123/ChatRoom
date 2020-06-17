package com.wlm.chatroom.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wlm.chatroom.common.Message

@Database(entities = [Message::class], version = 1)
abstract class ChatDb : RoomDatabase() {
    abstract fun messageDao() : MessageDao

    companion object {
        private var instance :ChatDb? = null

        @Synchronized
        fun get(context: Context): ChatDb {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, ChatDb::class.java, "ChatRoomDb.db")
                    .build()
            }
            return instance!!
        }
    }
}
