package com.wlm.chatroom.ui.fragment

import com.wlm.chatroom.R
import com.wlm.chatroom.base.ui.BaseVMFragment
import com.wlm.chatroom.viewModel.DiscussViewModel

class ContactFragment : BaseVMFragment<DiscussViewModel>() {
    override val layoutId = R.layout.fragment_message
    override val providerVMClass = DiscussViewModel::class.java

    override fun init() {
        super.init()

    }



}