package com.wlm.chatroom.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.wlm.chatroom.MyApp
import com.wlm.chatroom.base.BaseViewModel
import com.wlm.chatroom.base.UiState
import com.wlm.chatroom.common.Constant
import com.wlm.chatroom.common.Discuss
import com.wlm.chatroom.common.utils.SharedPrefs
import com.wlm.chatroom.executeResponse
import com.wlm.chatroom.repository.DiscussRepository
import com.wlm.chatroom.repository.UserRepository
import kotlinx.coroutines.launch

class DiscussViewModel : BaseViewModel() {

    private val loginRepository by lazy { UserRepository() }
    val discussRepository by lazy { DiscussRepository(this) }

    val loginState = MutableLiveData<Boolean>()

    private val pageSize = MutableLiveData<Int>()

    private val result = Transformations.map(pageSize) {
        discussRepository.getListingData(it)
    }

    val pagedList = Transformations.switchMap(result) { it.pagedList }


    val uiState = MutableLiveData<UiState<List<Discuss>>>()

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

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

                            MyApp.instance.currentUserId = it.id
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

    fun refresh() {
        result.value?.refresh?.invoke()

    }

    val addDiscussState = MutableLiveData<UiState<Boolean>>()

    fun addDiscuss(discussTitle: String, visibleType: Int) {

        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = discussRepository.addDiscuss(discussTitle, visibleType)
                    executeResponse(result, {
                        addDiscussState.value = UiState(false, null, true)

                    }, {
                        addDiscussState.value = UiState(false, result.errorMsg, false)
                    })
                },
                catchBlock = {
                    addDiscussState.value = UiState(false, it.message, false)
                }
            )
        }
    }

}