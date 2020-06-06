package com.wlm.chatroom.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.chatroom.base.BaseViewModel
import com.wlm.chatroom.common.Constant
import com.wlm.chatroom.common.utils.SharedPrefs
import com.wlm.chatroom.executeResponse
import com.wlm.chatroom.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {

    private val loginRepository by lazy { UserRepository() }
    private var isLogin by SharedPrefs(Constant.IS_LOGIN, false)

    val loginState = MutableLiveData<Boolean>()

    fun logout() {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = loginRepository.logout()
                    executeResponse(result, {
                        isLogin = false
                        loginState.value = false
                    }, {})
                }
            )
        }
    }

}