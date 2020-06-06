package com.wlm.chatroom.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import com.wlm.chatroom.R
import com.wlm.chatroom.base.ui.BaseVMActivity
import com.wlm.chatroom.common.utils.ToastUtils
import com.wlm.chatroom.ui.fragment.DiscussFragment
import com.wlm.chatroom.viewModel.DiscussViewModel
import kotlinx.android.synthetic.main.activity_add_discuss.*
import kotlinx.android.synthetic.main.layout_header.*

class AddDiscussActivity : BaseVMActivity<DiscussViewModel>() {
    override val layoutId: Int = R.layout.activity_add_discuss
    override val providerVMClass = DiscussViewModel::class.java


    override fun init() {
        super.init()
        headerToolbar.title = getString(R.string.str_add_discuss)
        headerToolbar.setNavigationIcon(R.drawable.arrow_back)
        headerToolbar.setNavigationOnClickListener { finish() }

        bindView()
    }

    private fun bindView() {
        btn_add_discuss.setOnClickListener {
            check()
            if (check()) {
                val visibleType = when (sp_discuss_type.selectedItemPosition) {
                    0 -> 1
                    else -> 2
                }
                mViewModel.addDiscuss(et_discuss_title.text.toString(), visibleType)
            }
        }
    }

    private fun check() : Boolean {
        if (et_discuss_title.text.toString().isBlank()) {
            tv_add_discuss_tips.text = "聊天室名称不能为空"
            tv_add_discuss_tips.visibility = View.VISIBLE
            return false
        } else {
            tv_add_discuss_tips.visibility = View.GONE
            return true
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.addDiscussState.observe(this, Observer {
            if (it.success != null && it.success) {
                DiscussFragment.listChanged = true
                finish()
            }
            it.error?.let { msg ->
                ToastUtils.show(msg)
            }
        })
    }
}
