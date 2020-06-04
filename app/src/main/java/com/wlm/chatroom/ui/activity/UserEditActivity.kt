package com.wlm.chatroom.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import com.wlm.chatroom.R
import com.wlm.chatroom.base.ui.BaseVMActivity
import com.wlm.chatroom.common.Constant
import com.wlm.chatroom.common.utils.SharedPrefs
import com.wlm.chatroom.common.utils.ToastUtils
import com.wlm.chatroom.viewModel.UserEditViewModel
import kotlinx.android.synthetic.main.activity_user_edit.*
import kotlinx.android.synthetic.main.layout_header.*

class UserEditActivity : BaseVMActivity<UserEditViewModel>() {
    override val layoutId: Int = R.layout.activity_user_edit
    override val providerVMClass = UserEditViewModel::class.java

    private var userString by SharedPrefs(Constant.USER_STRING, "")

    private val userInfo = userString.split(",")

    override fun init() {
        super.init()
        headerToolbar.title = getString(R.string.app_name)
        headerToolbar.setNavigationIcon(R.drawable.arrow_back)
        headerToolbar.setNavigationOnClickListener { finish() }
        bindView()
    }

    private fun bindView() {
        user_edit_nickname.setText(userInfo[3])
        user_edit_e_mail.setText(userInfo[4])
        user_edit_mobile.setText(userInfo[5])
        user_edit_password.setText(userInfo[1])

        btn_edit_user.setOnClickListener {

            val editMap = mutableMapOf(
                "id" to userInfo[2],
                "nickname" to user_edit_nickname.text.toString(),
                "email" to user_edit_e_mail.text.toString(),
                "mobile" to user_edit_mobile.text.toString()
            )
            if (ckb_change_password.isChecked) {
                editMap["password"] = user_edit_password.text.toString()
            }
            mViewModel.editUser(editMap)

        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            editState.observe(this@UserEditActivity, Observer {
                if (it) {
                    finish()
                    userString = "${userInfo[0]},${userInfo[1]},${userInfo[2]},${user_edit_nickname.text},${user_edit_e_mail.text},${user_edit_mobile.text}"
                } else ToastUtils.show("保存失败")
            })
        }
    }

}
