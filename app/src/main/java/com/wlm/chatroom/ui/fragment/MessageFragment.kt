package com.wlm.chatroom.ui.fragment

import androidx.lifecycle.Observer
import com.wlm.chatroom.R
import com.wlm.chatroom.base.ui.BaseVMFragment
import com.wlm.chatroom.common.Constant
import com.wlm.chatroom.common.utils.SharedPrefs
import com.wlm.chatroom.viewModel.MessageViewModel

class MessageFragment : BaseVMFragment<MessageViewModel>() {
    override val layoutId = R.layout.fragment_message
    override val providerVMClass = MessageViewModel::class.java

    private var isLogin by SharedPrefs(Constant.IS_LOGIN, false)



    override fun init() {
        super.init()
        initUser()
    }

    private fun initUser() {
        if (isLogin) {
            mViewModel.login()
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {


            loginState.observe(this@MessageFragment, Observer {
                isLogin = it
            })

        }
    }


}