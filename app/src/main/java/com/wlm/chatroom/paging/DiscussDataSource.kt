package com.wlm.chatroom.paging

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.chatroom.base.UiState
import com.wlm.chatroom.common.Discuss
import com.wlm.chatroom.executeResponse
import com.wlm.chatroom.viewModel.DiscussViewModel
import kotlinx.coroutines.launch

class DiscussDataSource(private val discussViewModel: DiscussViewModel) :
    ItemKeyedDataSource<Int, Discuss>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Discuss>
    ) {
        discussViewModel.run {
            viewModelScope.launch {
                uiState.value = UiState(true, null, null)
                tryCatch(
                    tryBlock = {
                        val result = discussRepository.getDiscussList()

                        executeResponse(result, {
                            val resultData = result.data ?: listOf()
                            uiState.value = UiState(false, null, resultData)
                            callback.onResult(resultData)

                        }, { msg ->
                            uiState.value = UiState(false, msg, null)
                        })
                    },
                    catchBlock = { t ->
                        uiState.value = UiState(false, t.message, null)
                    },
                    handleCancellationExceptionManually = true
                )
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Discuss>) {

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Discuss>) {

    }

    override fun getKey(item: Discuss): Int = item.discussId

}