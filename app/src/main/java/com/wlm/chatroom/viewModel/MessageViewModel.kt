package com.wlm.chatroom.viewModel

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.chatroom.MyApp
import com.wlm.chatroom.base.BaseViewModel
import com.wlm.chatroom.common.Message
import com.wlm.chatroom.common.User
import com.wlm.chatroom.db.ChatDb
import com.wlm.chatroom.executeResponse
import com.wlm.chatroom.repository.MessageRepository
import com.wlm.chatroom.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessageViewModel : BaseViewModel() {

    private val messageRepository by lazy { MessageRepository(this) }
    private val userRepository by lazy { UserRepository() }

    private val dao by lazy { ChatDb.get(MyApp.instance).messageDao() }

    val discussId = MutableLiveData<Int>()

    val messageList = Transformations.switchMap(discussId) {
        LivePagedListBuilder(
            dao.getAllMessage(it),
            PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)//配置分页加载的数量
                .setEnablePlaceholders(ENABLE_PLACEHOLDERS)//配置是否启动PlaceHolders
                .setInitialLoadSizeHint(PAGE_SIZE * 2)//初始化加载数量（默认3倍pagesize）
                .build()
        ).build()
    }


    //1 -1刷新消息列表成功失败
    //2 -2发送消息成功失败
    val messageState = MutableLiveData<Int>()

    //1：聊天界面，2：添加聊天室成员界面，3：删除聊天室成员界面
    val uiState = MutableLiveData<Int>()

    //1：添加成功，-1：添加失败
    val discussManState = MutableLiveData<Int>()

    fun sendMsg(msg: String, discussId: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = messageRepository.sendMsg(msg, discussId)
                    executeResponse(result, {

                        messageState.value = 2

                    }, {

                        messageState.value = -2
                    })
                },
                catchBlock = {}
            )
        }
    }

    fun refreshMsg(discussId: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = messageRepository.getMsgList(discussId)
                    executeResponse(result, {
                        messageState.value = 1
                        result.data?.let {
                            withContext(Dispatchers.IO) {
                                val addList = mutableListOf<Message>()
                                val dbList = dao.getMessageList(discussId)
                                it.forEach {  msg ->
                                    if (!dbList.contains(msg)) {
                                        addList.add(msg)
                                    }
                                }
                                if (addList.size > 0) {
                                    dao.insert(addList)
                                }
                            }
                        }
                    }, {
                        messageState.value = -1
                    })
                },
                catchBlock = {
                    messageState.value = -1
                }
            )
        }
    }

    fun addDiscussMan(discussId: Int, userId: String) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = messageRepository.addDiscussMan(discussId, userId)
                    executeResponse(result, {
                        discussManState.value = 1

                    }, {
                        discussManState.value = -1
                    })
                },
                catchBlock = {
                    discussManState.value = -1
                }
            )
        }
    }

    val userList = MutableLiveData<List<User>?>()

    fun getUserList() {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = userRepository.userList()
                    executeResponse(result, {
                        result.data?.let {
                            userList.value = it
                        }

                    }, {
                        userList.value = null
                    })
                },
                catchBlock = {
                    userList.value = null
                }
            )
        }
    }

    fun getManList(discussId: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = messageRepository.getManList(discussId)
                    executeResponse(result, {


                    }, {

                    })
                },
                catchBlock = {

                }
            )
        }
    }

    companion object {
        private const val PAGE_SIZE = 15
        private const val ENABLE_PLACEHOLDERS = false
    }
}