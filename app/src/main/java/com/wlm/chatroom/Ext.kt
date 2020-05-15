package com.wlm.chatroom

import com.wlm.chatroom.common.net.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

suspend fun<T> executeResponse(
    response: HttpResponse<T>,
    successBlock: suspend CoroutineScope.() -> Unit,
    errorBlock: suspend CoroutineScope.(String?) -> Unit
) {
    coroutineScope {
        if (response.errorCode == -1) errorBlock(response.errorMsg)
        else successBlock()
    }
}

