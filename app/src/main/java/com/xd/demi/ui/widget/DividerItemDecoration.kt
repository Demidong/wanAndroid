package com.xd.demi.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.xd.demi.todo.R


class DividerItemDecoration
@JvmOverloads constructor(context: Context,
                          val orientation: Int = LinearLayoutManager.VERTICAL,
                          val itemSize: Float = SizeUtils.dp2px(1.0f).toFloat(),
                          val colorId: Int = R.color.grey) : RecyclerView.ItemDecoration() {

    /**
     * 绘制item分割线的画笔，和设置其属性
     * 来绘制个性分割线
     */
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    public var isDrawFirst: Boolean = true
    public var isDrawLast: Boolean = false

    init {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw IllegalArgumentException("请传入正确的参数")
        }
        paint.color = ContextCompat.getColor(context, colorId)
        /*设置填充*/
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        if (orientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    /**
     * 绘制纵向 item 分割线
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = (child.bottom + layoutParams.bottomMargin).toFloat()
            val bottom = top + itemSize
            if (!isDrawFirst && 0 == parent.getChildAdapterPosition(parent.getChildAt(i))) {
                return
            }
            if (!isDrawLast && parent.childCount - 1 == parent.getChildAdapterPosition(parent.getChildAt(i))) {
                return
            }
            canvas.drawRect(left.toFloat(), top, right.toFloat(), bottom, paint)
        }
    }

    /**
     * 绘制横向 item 分割线
     */
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.width - parent.paddingBottom
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = (child.right + layoutParams.rightMargin).toFloat()
            val right = left + itemSize
            if (!isDrawFirst && 0 == parent.getChildAdapterPosition(parent.getChildAt(i))) {
                return
            }
            if (!isDrawLast && parent.childCount - 1 == parent.getChildAdapterPosition(parent.getChildAt(i))) {
                return
            }
            canvas.drawRect(left, top.toFloat(), right, bottom.toFloat(), paint)
        }
    }


}