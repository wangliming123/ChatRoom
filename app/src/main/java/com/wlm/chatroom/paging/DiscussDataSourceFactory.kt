package com.wlm.chatroom.paging

import com.wlm.chatroom.common.Discuss
import com.wlm.chatroom.viewModel.DiscussViewModel

class DiscussDataSourceFactory(private val discussViewModel: DiscussViewModel) : BaseDataSourceFactory<DiscussDataSource, Discuss>() {

    override fun createDataSource(): DiscussDataSource =
        DiscussDataSource(discussViewModel)

}