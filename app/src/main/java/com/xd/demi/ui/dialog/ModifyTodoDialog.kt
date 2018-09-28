package com.xd.demi.ui.dialog

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.xd.demi.base.BaseDialog
import com.xd.demi.todo.R
import kotlinx.android.synthetic.main.dialog_modify_todo.*

/**
 * 新人福利
 * Created by Chen on 2018/7/23.
 */
class ModifyTodoDialog(context: Context) : BaseDialog(context) {

     var content: String? = null
     var title: String? = null
     var date: String? = null

    override fun getLayoutId() = R.layout.dialog_modify_todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sb_cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun setWindowParams() {
        window?.let {
            val attrs = it.attributes
            attrs.width = ViewGroup.LayoutParams.MATCH_PARENT
            attrs.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attrs.gravity = Gravity.CENTER
            window.attributes = attrs
        }
    }
    fun setETTitle(title: String) {
        this.title = title
        et_title.setText(title)
    }

    fun setETContent(content: String) {
        this.content = content
        et_content.setText(content)
    }

    fun setETDate(date: String) {
        this.date = date
        et_date.setText(date)
    }

    fun setOnUpdateListener(listener: View.OnClickListener){
        sb_save.setOnClickListener (listener)
    }


}