package com.wlm.chatroom.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.chatroom.base.BaseViewModel
import com.wlm.chatroom.common.Constant
import com.wlm.chatroom.common.utils.SharedPrefs
import com.wlm.chatroom.executeResponse
import com.wlm.chatroom.repository.LoginRepository
import kotlinx.coroutines.launch

class MessageViewModel : BaseViewModel() {

//    val messageRepository by lazy { MessageRepository(this) }

    private val loginRepository by lazy { LoginRepository() }

    val loginState = MutableLiveData<Boolean>()


    fun login() {
        var userString by SharedPrefs(Constant.USER_STRING, "")
        if (userString.isEmpty()) {
            loginState.value = false
        }
        val namePassword = userString.split(",")
        val username = namePassword[0]
        val password = namePassword[1]
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = loginRepository.login(username, password)
                    executeResponse(result, {
                        loginState.value = true
                        result.data?.let {
                            userString = "$username,$password,${it.id},${it.nickname},${it.email},${it.mobile}"
                        }

                    }, {
                        loginState.value = false
                    })
                },
                catchBlock = {
                    loginState.value = false
                }
            )
        }
    }

}