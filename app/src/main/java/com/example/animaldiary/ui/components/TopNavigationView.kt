package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.animaldiary.R

class TopNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    val leftIcon: ImageView
    val rightIcon1: ImageView
    val rightIcon2: ImageView
    val titleText: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.component_top_navigation, this, true)

        leftIcon = findViewById(R.id.nav_left_icon)
        rightIcon1 = findViewById(R.id.nav_right_icon_1)
        rightIcon2 = findViewById(R.id.nav_right_icon_2)
        titleText = findViewById(R.id.nav_title)

        // 기본적으로 모든 아이콘을 숨김
        leftIcon.visibility = View.GONE
        rightIcon1.visibility = View.GONE
        rightIcon2.visibility = View.GONE

        // attrs.xml 속성 처리
        attrs?.let {
            val tn = context.obtainStyledAttributes(it, R.styleable.TopNavigationView)
            try {
                // 제목 설정
                val title = tn.getString(R.styleable.TopNavigationView_tnv_title)
                val showTitle = tn.getBoolean(R.styleable.TopNavigationView_tnv_showTitle, true)

                if (title != null && showTitle) {
                    setTitle(title)
                } else {
                    setTitleVisible(false)
                }

                val leftIconRes = tn.getResourceId(R.styleable.TopNavigationView_tnv_leftIcon, -1)
                if (leftIconRes != -1) {
                    setLeftIcon(leftIconRes)
                }

                val rightIcon1Res = tn.getResourceId(R.styleable.TopNavigationView_tnv_rightIcon1, -1)
                if (rightIcon1Res != -1) {
                    setRightIcon1(rightIcon1Res)
                }

                val rightIcon2Res = tn.getResourceId(R.styleable.TopNavigationView_tnv_rightIcon2, -1)
                if (rightIcon2Res != -1) {
                    setRightIcon2(rightIcon2Res)
                }
            } finally {
                tn.recycle()
            }
        }
    }

    fun setTitle(text: String) {
        titleText.text = text
        titleText.visibility = View.VISIBLE
    }

    fun setTitleVisible(visible: Boolean) {
        titleText.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setLeftIcon(resId: Int, onClick: (() -> Unit)? = null) {
        leftIcon.setImageResource(resId)
        leftIcon.visibility = View.VISIBLE
        onClick?.let { leftIcon.setOnClickListener { it() } }
    }

    fun setRightIcon1(resId: Int, onClick: (() -> Unit)? = null) {
        rightIcon1.setImageResource(resId)
        rightIcon1.visibility = View.VISIBLE
        onClick?.let { rightIcon1.setOnClickListener { it() } }
    }

    fun setRightIcon2(resId: Int, onClick: (() -> Unit)? = null) {
        rightIcon2.setImageResource(resId)
        rightIcon2.visibility = View.VISIBLE
        onClick?.let { rightIcon2.setOnClickListener { it() } }
    }

    fun setLeftIconVisible(visible: Boolean) {
        leftIcon.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setRightIcon1Visible(visible: Boolean) {
        rightIcon1.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setRightIcon2Visible(visible: Boolean) {
        rightIcon2.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
