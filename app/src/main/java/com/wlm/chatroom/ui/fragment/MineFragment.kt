package com.wlm.chatroom.ui.fragment

import com.wlm.chatroom.R
import com.wlm.chatroom.base.ui.BaseVMFragment
import com.wlm.chatroom.common.Constant
import com.wlm.chatroom.common.utils.SharedPrefs
import com.wlm.chatroom.viewModel.MessageViewModel

class MineFragment : BaseVMFragment<MessageViewModel>() {
    override val layoutId = R.layout.fragment_mine
    override val providerVMClass = MessageViewModel::class.java

    private var isLogin by SharedPrefs(Constant.IS_LOGIN, false)

    override fun init() {
        super.init()

    }



}