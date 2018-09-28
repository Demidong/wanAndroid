package com.xd.demi.ui.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xd.demi.bean.Event
import com.xd.demi.bean.TodoData
import com.xd.demi.commen.subscribeResp
import com.xd.demi.network.RetrofitManager
import com.xd.demi.todo.R
import com.xd.demi.ui.dialog.ModifyTodoDialog
import com.xd.demi.ui.widget.DividerItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_modify_todo.*
import kotlinx.android.synthetic.main.item_todo_outer.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by demi on 2018/8/11 下午3:51.
 */
class TodoAdapter : BaseQuickAdapter<TodoData, BaseViewHolder>(R.layout.item_todo_outer) {

    override fun convert(helper: BaseViewHolder, item: TodoData) {
        val recy = RecyclerView(mContext)
        recy.layoutManager = LinearLayoutManager(mContext)
        val adapter = EventAdapter()
        recy.adapter = adapter
        recy.addItemDecoration((DividerItemDecoration(context = mContext, itemSize = SizeUtils.dp2px(1.0f).toFloat())))
        adapter.setNewData(item.todoList)
        helper.itemView.expand.setContent(recy)
        helper.itemView.expand.setName(TimeUtils.millis2String(item.date, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())))
        adapter.setOnItemClickListener { adapter, view, position ->
            val dialog = ModifyTodoDialog(mContext)
            dialog.show()
            val event = adapter.getItem(position) as Event
            dialog.setETTitle(event.title)
            dialog.setETContent(event.content)
            dialog.setETDate(event.dateStr)
            dialog.setOnUpdateListener(View.OnClickListener {
                dialog.title = dialog.et_title.text.toString()
                dialog.content = dialog.et_content.text.toString()
                dialog.date = dialog.et_date.text.toString()
                RetrofitManager.model
                        .update(event.id,dialog.title,dialog.content,event.status,event.type,dialog.date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeResp(onNext = {
                            dialog.dismiss()
                            adapter.setData(position,it)
                        },onError = {
                            dialog.dismiss()
                            LogUtils.e(it.message)
                        })

            })

        }
        adapter.setOnItemChildClickListener { adapter, view, position ->

        }
    }

}