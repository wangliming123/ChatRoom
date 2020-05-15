package com.wlm.chatroom.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.chatroom.base.UiState
import com.wlm.chatroom.base.BaseViewModel
import com.wlm.chatroom.executeResponse
import com.wlm.chatroom.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    private val repository by lazy { LoginRepository() }
    val uiState = MutableLiveData<UiState<String>>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            uiState.value = UiState(true, null, null)
            tryCatch(
                tryBlock = {
                    val result = repository.login(username, password)
                    executeResponse(result, {
                        result.data?.let {
                            uiState.value = UiState(false, null, "$username,$password")
                        }
                    }, {
                        uiState.value = UiState(false, "登录失败", null)
                    })
                },
                catchBlock = {
                    uiState.value = UiState(false, "登录失败", null)
                },
                handleCancellationExceptionManually = true
            )

        }
    }
//
    fun register(username: String, password: String) {
        viewModelScope.launch {
            uiState.value = UiState(true, null, null)
            tryCatch(
                tryBlock = {
                    val result = repository.register(username, password, password)
                    executeResponse(result, {
                        result.data?.let {
                            uiState.value = UiState(false, null, "$username,$password")
                            repository.login(username, password)
                        }
                    }, {
                        uiState.value = UiState(false, "注册失败", null)
                    })
                },
                catchBlock = {
                    uiState.value = UiState(false, "注册失败", null)
                },
                handleCancellationExceptionManually = true
            )

        }
    }
}