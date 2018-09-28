package com.xd.demi.ui.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.xd.demi.todo.R


/**
 * Created by demi on 17/4/24.
 */
class CollapsExpandLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private var number_text: TextView? = null
    private var title_name: TextView? = null
    private var arrow: ImageView? = null
    private var content: RelativeLayout? = null
    private var title_layout: RelativeLayout? = null
    private val parentWidthMeasureSpec: Int = 0
    private val parentHeightMeasureSpec: Int = 0
    private var contentWidthMeasureSpec: Int = 0
    private var contentHeightMeasureSpec: Int = 0
    private val duration = 500
    private var lastClickTime: Long = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.collaps_item, this)
        initView()
        collaps(content!!)
    }

    private fun initView() {
//        number_text = findViewById<View>(R.id.number) as TextView
        title_name = findViewById<View>(R.id.tv_date) as TextView
        arrow = findViewById<View>(R.id.arrow) as ImageView
        title_layout = findViewById<View>(R.id.title_layout) as RelativeLayout
        content = findViewById<View>(R.id.content) as RelativeLayout
        title_layout!!.setOnClickListener { rotateArrow(System.currentTimeMillis()) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        contentWidthMeasureSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, 0, ViewGroup.LayoutParams.MATCH_PARENT)
        contentHeightMeasureSpec = ViewGroup.getChildMeasureSpec(heightMeasureSpec, 0, height - getChildAt(0).measuredHeight)
    }

    private fun expand(view: View) {
        view.visibility = View.VISIBLE
        view.measure(contentWidthMeasureSpec, contentHeightMeasureSpec)
        val height = view.measuredHeight
        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    view.layoutParams.height = height
                } else {
                    view.layoutParams.height = (height * interpolatedTime).toInt()
                }
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        animation.duration = duration.toLong()
        view.startAnimation(animation)
    }

    private fun collaps(view: View) {
        val measureheight = view.measuredHeight
        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    view.visibility = View.GONE
                } else {
                    view.layoutParams.height = measureheight - (measureheight * interpolatedTime).toInt()
                }
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        animation.duration = duration.toLong()
        view.startAnimation(animation)
    }

    private fun rotateArrow(time: Long) {
        if (time - lastClickTime < duration) {
            return
        }
        val degree: Int
        if (content!!.visibility == View.GONE) {
            expand(content!!)
            degree = 90
        } else {
            collaps(content!!)
            degree = 0
        }
        arrow!!.animate().setDuration(duration.toLong()).rotation(degree.toFloat())
        lastClickTime = time
    }

    fun setNumber(num: String) {
        if (!TextUtils.isEmpty(num)) {
            number_text!!.text = num
        }
    }

    fun setName(text: String) {
        if (!TextUtils.isEmpty(text)) {
            title_name!!.text = text
        }
    }

    fun setContent(imageView: ImageView) {
        val layoutParams = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        imageView.layoutParams = layoutParams
        if (content!!.childCount > 0) {
            content!!.removeAllViews()
        }
        content!!.addView(imageView)
    }

    fun setContent(recy: RecyclerView) {
        val layoutParams = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        recy.layoutParams = layoutParams
        if (content!!.childCount > 0) {
            content!!.removeAllViews()
        }
        content!!.addView(recy)
    }
}
