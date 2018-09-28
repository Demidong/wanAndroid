package com.xd.demi.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import com.xd.demi.todo.R

/**
 * Created by tong on 2018/2/12.
 */
abstract class BaseDialog(context: Context, themeResId: Int = R.style.Theme_Design_BottomSheetDialog) : Dialog(context, themeResId) {
    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setWindowParams()
    }

    open fun setWindowParams() {
        window?.let {
            val attrs = it.attributes
            attrs.width = ViewGroup.LayoutParams.MATCH_PARENT
            attrs.gravity = Gravity.CENTER
            window.attributes = attrs
        }
    }
}