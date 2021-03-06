package com.wlm.chatroom.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import com.wlm.chatroom.R
import com.wlm.chatroom.viewModel.LoginViewModel
import com.wlm.chatroom.base.ui.BaseVMActivity
import com.wlm.chatroom.common.Constant
import com.wlm.chatroom.common.utils.SharedPrefs
import com.wlm.chatroom.common.utils.ToastUtils
import com.wlm.chatroom.startKtxActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class LoginActivity : BaseVMActivity<LoginViewModel>() {
    override val providerVMClass: Class<LoginViewModel> = LoginViewModel::class.java
    override val layoutId: Int = R.layout.activity_login

    private var isLogin by SharedPrefs(Constant.IS_LOGIN, false)
    private var userString by SharedPrefs(Constant.USER_STRING, "")

    override fun init() {
        super.init()
        initView()
        setSupportActionBar(toolbar)
//        toolbar.navigationIcon = getDrawable(R.drawable.arrow_back)
//        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initView() {
        btn_login.setOnClickListener { login() }
        btn_register.setOnClickListener { register() }
    }

    private fun login() {
        if (check()) {
            mViewModel.login(et_username.text.toString(), et_password.text.toString())
        }
    }

    private fun register() {
        if (check()) {
            mViewModel.register(et_username.text.toString(), et_password.text.toString())
        }
    }

    private fun check(): Boolean {
        if (et_username.text.toString().length < 3) {
            tv_tips.visibility = View.VISIBLE
            tv_tips.text = getString(R.string.str_username_invalid)
            return false
        }
        if (et_password.text.toString().length < 3) {
            tv_tips.visibility = View.VISIBLE
            tv_tips.text = getString(R.string.str_password_invalid)
            return false
        }
        tv_tips.visibility = View.GONE
        return true
    }


    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            uiState.observe(this@LoginActivity, Observer { state ->
                showLoading(state.loading)

                state.error?.let {
                    ToastUtils.show(it)
                }

                state.success?.let {
                    isLogin = true
                    userString = it
                    startKtxActivity<MainActivity>()
                    finish()
                }
            })
        }
    }

    private fun showLoading(loading: Boolean) {
        if (loading) loginProgress.visibility = View.VISIBLE else loginProgress.visibility = View.GONE
    }

    override fun onError(e: Throwable) {
        Logger.d("login or register error", e.message)
    }
}
