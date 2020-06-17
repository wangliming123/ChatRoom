package com.wlm.chatroom.ui.activity

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import com.wlm.chatroom.R
import com.wlm.chatroom.base.ui.BaseVMActivity
import com.wlm.chatroom.common.User
import com.wlm.chatroom.common.utils.ToastUtils
import com.wlm.chatroom.ui.adapter.MessageAdapter
import com.wlm.chatroom.ui.adapter.OnItemCheckedListener
import com.wlm.chatroom.ui.adapter.UserAdapter
import com.wlm.chatroom.viewModel.MessageViewModel
import kotlinx.android.synthetic.main.activity_discuss_message.*
import kotlinx.android.synthetic.main.layout_header.*

class DiscussMessageActivity : BaseVMActivity<MessageViewModel>() {
    companion object {
        const val KEY_DISCUSS_ID = "KEY_DISCUSS_ID"
        const val KEY_DISCUSS_TITLE = "KEY_DISCUSS_TITLE"
    }

    override val layoutId: Int = R.layout.activity_discuss_message
    override val providerVMClass = MessageViewModel::class.java

    private var discussId: Int = 0
    private var users: MutableList<User> = mutableListOf()

    private val messageAdapter by lazy { MessageAdapter() }
    private var userAdapter = UserAdapter(users)

    private var currentCheckPosition: Int = 0
    private var currentChecked: Boolean = false
    private var currentAddUser: User? = null

    override fun init() {
        Logger.d(System.currentTimeMillis())
        super.init()

        setSupportActionBar(headerToolbar)

        Logger.d(System.currentTimeMillis())
        discussId = intent!!.extras!!.getInt(KEY_DISCUSS_ID)
        headerToolbar.title = intent!!.extras!!.getString(KEY_DISCUSS_TITLE)
        headerToolbar.setNavigationIcon(R.drawable.arrow_back)
        headerToolbar.setNavigationOnClickListener {
            when(mViewModel.uiState) {
                1 -> finish()
                else -> {
                    tab_message.visibility = View.VISIBLE
                    rv_user.visibility = View.GONE
                    rv_discuss_man.visibility = View.GONE
                }
            }
        }
        bindView()

        Logger.d(System.currentTimeMillis())
        mViewModel.refreshMsg(discussId)
        mViewModel.discussId.value = this.discussId

        Logger.d(System.currentTimeMillis())
    }

    private fun bindView() {

        rv_msg.adapter = messageAdapter
        rv_user.adapter = userAdapter
        btn_send_msg.setOnClickListener {
            if (et_message.text.toString().isBlank()) {
                et_message.requestFocus()
            } else {
                rv_msg.scrollToPosition(messageAdapter.itemCount - 1)
                mViewModel.sendMsg(et_message.text.toString(), discussId)
            }
        }
        userAdapter.setOnItemCheckedListener(object : OnItemCheckedListener {
            override fun onChecked(
                isChecked: Boolean,
                item: User,
                position: Int
            ) {
                currentCheckPosition = position
                currentChecked = isChecked
                currentAddUser = item
                if (isChecked) {
                    mViewModel.addDiscussMan(discussId, item.id)
                }
            }

        })
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            messageList.observe(this@DiscussMessageActivity, Observer {
                messageAdapter.submitList(it)
            })

            messageState.observe(this@DiscussMessageActivity, Observer {
                when(it) {
                    1 -> {
                        Logger.d("消息刷新成功")
                    }
                    2 -> {
                        Logger.d("消息发送成功")
                        et_message.setText("")
                        refreshMsg(this@DiscussMessageActivity.discussId)
                    }
                    -1 -> {
                        Logger.d("消息刷新失败")
                    }
                    -2 -> {
                        Logger.d("消息发送失败")
                        et_message.requestFocus()
                    }
                }
            })

            uiState.observe(this@DiscussMessageActivity, Observer {
                when(it) {
                    1 -> {
                        tab_message.visibility = View.VISIBLE
                        rv_user.visibility = View.GONE
                        rv_discuss_man.visibility = View.GONE
                    }
                    2 -> {
                        tab_message.visibility = View.GONE
                        rv_user.visibility = View.VISIBLE
                        rv_discuss_man.visibility = View.GONE
                        mViewModel.getUserList()
                        mViewModel.getManList(this@DiscussMessageActivity.discussId)
                    }
                    3 -> {

                    }
                }
            })

            userList.observe(this@DiscussMessageActivity, Observer {
                it?.let {
                    users.clear()
                    users.addAll(it)
                    userAdapter.notifyDataSetChanged()
                }
            })

            discussManState.observe(this@DiscussMessageActivity, Observer {
                when(it) {
                    1 -> {
                        ToastUtils.show("添加成功")
                        Logger.d("聊天室人员添加成功")
                    }
                    -1 -> {
                        ToastUtils.show("添加失败")
                        Logger.d("聊天室人员添加失败")
                        userAdapter.changItemCheck(currentCheckPosition, !currentChecked)
                    }
                }
            })

            mViewModel.uiState.value = 1
//            mViewModel.refreshMsg(discussId)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_discuss_message, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.discuss_man_add -> {
               mViewModel.uiState.value = 2
            }

//            R.id.discuss_man_delete -> {
//                mViewModel.uiState.value = 3
//            }
        }
        return super.onOptionsItemSelected(item)
    }
}
