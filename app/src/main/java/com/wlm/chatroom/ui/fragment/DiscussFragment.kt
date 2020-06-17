package com.wlm.chatroom.ui.fragment

import android.graphics.Color
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.chatroom.R
import com.wlm.chatroom.base.ui.BaseVMFragment
import com.wlm.chatroom.common.Constant
import com.wlm.chatroom.common.utils.SharedPrefs
import com.wlm.chatroom.ui.adapter.DiscussAdapter
import com.wlm.chatroom.viewModel.DiscussViewModel
import kotlinx.android.synthetic.main.refresh_layout.*

class DiscussFragment : BaseVMFragment<DiscussViewModel>() {
    override val layoutId = R.layout.refresh_layout
    override val providerVMClass = DiscussViewModel::class.java

    override fun childMultipleStatusView(): MultipleStatusView? = multiple_status_view

    private var isLogin by SharedPrefs(Constant.IS_LOGIN, false)

    private val adapter by lazy { DiscussAdapter() }


    override fun init() {
        super.init()
        initUser()
        initRecyclerView()


        layout_refresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        layout_refresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }
    }


    private fun initUser() {
        if (isLogin) {
            mViewModel.login()
        }
    }

    private fun initRecyclerView() {

        rv_refresh.adapter = adapter
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            pagedList.observe(this@DiscussFragment, Observer {
                adapter.submitList(it)
            })
            uiState.observe(this@DiscussFragment, Observer { state->
                layout_refresh.isRefreshing = state.loading

                if (state.loading) {
                    if (isRefreshFromPull) {
                        isRefreshFromPull = false
                    } else {
                        multipleStatusView?.showLoading()
                    }
                }

                state.success?.let {
                    if (it.isEmpty()) {
                        multipleStatusView?.showEmpty()
                    }else {
                        multipleStatusView?.showContent()
                    }
                }

                state.error?.let {
                    multipleStatusView?.showError()
                    Logger.d("load_error", it)
                }
            })


            loginState.observe(this@DiscussFragment, Observer {
                isLogin = it
            })

            initLoad()

        }
    }

    override fun retry() {
        mViewModel.refresh()
    }

    override fun onResume() {
        super.onResume()
        if (listChanged) {
            listChanged = false
            mViewModel.refresh()
        }
    }

    companion object {
        var listChanged = false;
    }
}