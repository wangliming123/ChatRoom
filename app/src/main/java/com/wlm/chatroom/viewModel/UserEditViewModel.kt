package com.wlm.chatroom.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.chatroom.base.BaseViewModel
import com.wlm.chatroom.executeResponse
import com.wlm.chatroom.repository.UserRepository
import kotlinx.coroutines.launch

class UserEditViewModel : BaseViewModel() {


    private val loginRepository by lazy { UserRepository() }


    val editState = MutableLiveData<Boolean>()

    fun editUser(editMap: Map<String, String>) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = loginRepository.editUser(editMap)
                    executeResponse(result, {
                        editState.value = true
                    }, {
                        editState.value = false
                    })
                },
                catchBlock = {
                    editState.value = false
                }
            )

        }

    }
}