package com.xd.demi.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xd.demi.bean.Event
import com.xd.demi.todo.R
import kotlinx.android.synthetic.main.item_todo_info.view.*

/**
 * Created by demi on 2018/8/11 下午3:51.
 */
class EventAdapter : BaseQuickAdapter<Event, BaseViewHolder>(R.layout.item_todo_info) {

    override fun convert(helper: BaseViewHolder, item: Event) {
        helper.itemView.tv_title.text = item.title
        helper.itemView.tv_info.text = item.content
        helper.itemView.tv_time.text = item.dateStr
    }

}