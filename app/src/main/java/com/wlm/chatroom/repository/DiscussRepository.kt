package com.wlm.chatroom.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.chatroom.MyApp
import com.wlm.chatroom.base.BaseRepository
import com.wlm.chatroom.common.Discuss
import com.wlm.chatroom.common.net.HttpResponse
import com.wlm.chatroom.common.net.RetrofitManager
import com.wlm.chatroom.paging.DiscussDataSourceFactory
import com.wlm.chatroom.paging.Listing
import com.wlm.chatroom.viewModel.DiscussViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DiscussRepository(private val discussViewModel: DiscussViewModel) : BaseRepository() {

    private val sourceFactory by lazy { DiscussDataSourceFactory(discussViewModel) }

    fun getListingData(pageSize: Int): Listing<Discuss> {
        val pageList = LivePagedListBuilder(
            sourceFactory,
            PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()
        ).build()
        return Listing(pageList, refresh = { sourceFactory.sourceLivaData.value?.invalidate() })
    }

    suspend fun getDiscussList(): HttpResponse<List<Discuss>> {
        return withContext(Dispatchers.IO) { RetrofitManager.service.getDiscussList(MyApp.instance.currentUserId) }
    }

    suspend fun addDiscuss(discussTitle: String, visibleType: Int): HttpResponse<Any> {
        return withContext(Dispatchers.IO) { RetrofitManager.service.addDiscuss(discussTitle, MyApp.instance.currentUserId, visibleType) }
    }

}