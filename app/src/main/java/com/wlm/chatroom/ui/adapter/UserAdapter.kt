package com.wlm.chatroom.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wlm.chatroom.R
import com.wlm.chatroom.common.User

class UserAdapter(private val users: List<User>) : RecyclerView.Adapter<UserViewHolder>() {

    private var listenerValid = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(parent)

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
        holder.userCheck.setOnCheckedChangeListener { _, isChecked ->
            users[position].checked = isChecked
            if (listenerValid) {
                listener?.onChecked(isChecked, users[position], position)
            } else {
                listenerValid = true
            }
        }
    }
    private var listener: OnItemCheckedListener? = null
    fun setOnItemCheckedListener(listener: OnItemCheckedListener) {
        this.listener = listener;
    }

    fun changItemCheck(position: Int, checked: Boolean) {
        listenerValid = false
        users[position].checked = checked
        notifyItemChanged(position)
    }


}


class UserViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
) {

    private val context = parent.context

    internal val userCheck = itemView.findViewById<CheckBox>(R.id.user_check)
    private val tvUserName = itemView.findViewById<TextView>(R.id.tv_item_user_name)

    fun bind(item: User?) {
        item?.run {
            tvUserName.text = userName
        }
    }

}

interface OnItemCheckedListener {
    fun onChecked(
        isChecked: Boolean,
        item: User,
        position: Int
    )
}