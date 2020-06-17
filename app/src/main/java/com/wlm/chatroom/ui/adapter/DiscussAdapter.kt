package com.wlm.chatroom.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.chatroom.R
import com.wlm.chatroom.common.Discuss
import com.wlm.chatroom.startKtxActivity
import com.wlm.chatroom.ui.activity.DiscussMessageActivity

class DiscussAdapter : PagedListAdapter<Discuss, DiscussViewHolder>(diffCallback) {

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<Discuss>() {
            override fun areItemsTheSame(oldItem: Discuss, newItem: Discuss): Boolean =
                oldItem.discussId == newItem.discussId

            override fun areContentsTheSame(oldItem: Discuss, newItem: Discuss): Boolean =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussViewHolder {
        return DiscussViewHolder(parent)
    }

    override fun onBindViewHolder(holder: DiscussViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiscussViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_discuss, parent, false)
) {

    private val context = parent.context

    private val tvDiscussTitle = itemView.findViewById<TextView>(R.id.tv_discuss_title)
    private val tvDiscussLatestDate = itemView.findViewById<TextView>(R.id.tv_discuss_latest_date)
    private val tvDiscussLatestText = itemView.findViewById<TextView>(R.id.tv_latest_discuss_text)

    fun bind(item: Discuss?) {
        item?.let { discuss ->
            tvDiscussTitle.text = discuss.discussTitle

            itemView.setOnClickListener {
                context.startKtxActivity<DiscussMessageActivity>(
                    values = listOf(
                        DiscussMessageActivity.KEY_DISCUSS_ID to discuss.discussId,
                        DiscussMessageActivity.KEY_DISCUSS_TITLE to discuss.discussTitle
                    )
                )
            }
        }
    }

}