package com.xd.demi.ui.activity

import android.app.Activity
import android.database.DataSetObserver
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.xd.demi.bean.Data
import com.xd.demi.bean.TodoData
import com.xd.demi.commen.subscribeResp
import com.xd.demi.network.RetrofitManager
import com.xd.demi.todo.R
import com.xd.demi.ui.adapter.TodoAdapter
import com.xd.demi.ui.widget.DividerItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by demi on 2018/8/11 下午2:30.
 */
class MainActivity : Activity() {

    private lateinit var adapter: TodoAdapter
    private  var type :Int= 0
    private var data: Data? = null
    private var status: Int= 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        refresh_layout.setOnRefreshListener {
            getTodoList()
        }
        recy_todo.layoutManager = LinearLayoutManager(this)
        adapter = TodoAdapter()
        recy_todo.adapter = adapter
        recy_todo.addItemDecoration((DividerItemDecoration(this, itemSize = SizeUtils.dp2px(1.0f).toFloat())))
        getTodoList()
        spinner_left.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0){
                    adapter.setNewData(data?.todoList)
                }else{
                    adapter.setNewData(data?.doneList)
                }
                status = position
            }

        }
        spinner_right.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = position
                getTodoList()
            }

        }
    }

    private fun getTodoList() {
        RetrofitManager.model
                .getToadListByType(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeResp(onNext = {
                    refresh_layout.finishRefresh()
                    data = it
                    if (status == 0){
                        adapter.setNewData(it.todoList)
                    }else{
                        adapter.setNewData(it.doneList)
                    }
                }, onError = {
                    LogUtils.e(it.message)
                    refresh_layout.finishRefresh()
                })
    }

}