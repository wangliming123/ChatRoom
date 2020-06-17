package com.wlm.chatroom.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.chatroom.MyApp
import com.wlm.chatroom.R
import com.wlm.chatroom.common.Message

class MessageAdapter : PagedListAdapter<Message, MessageViewHolder>(diffCallback) {
    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem.discussId == newItem.discussId

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem == newItem

        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MessageViewHolder(parent)

}

class MessageViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
) {

    private val context = parent.context

    private val tvLeftNickName = itemView.findViewById<TextView>(R.id.tv_left_nick_name)
    private val tvRightNickName = itemView.findViewById<TextView>(R.id.tv_right_nick_name)
    private val tvLeftMsg = itemView.findViewById<TextView>(R.id.tv_left_msg)
    private val tvRightMsg = itemView.findViewById<TextView>(R.id.tv_right_msg)
    private val tabLeft = itemView.findViewById<RelativeLayout>(R.id.tab_left_msg)
    private val tabRight = itemView.findViewById<RelativeLayout>(R.id.tab_right_msg)

    fun bind(item: Message?) {
        item?.run {
            if (userId == MyApp.instance.currentUserId) {
                tabLeft.visibility = View.GONE
                tabRight.visibility = View.VISIBLE
                tvRightMsg.text = msgContent
                tvRightNickName.text = nickname
            } else {
                tabRight.visibility = View.GONE
                tabLeft.visibility = View.VISIBLE
                tvLeftMsg.text = msgContent
                tvLeftNickName.text = nickname
            }
        }
    }

}
