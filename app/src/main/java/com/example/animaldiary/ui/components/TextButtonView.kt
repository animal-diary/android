package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.example.animaldiary.R

class TextButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val leftIcon: ImageView
    private val rightIcon: ImageView
    private val textView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.component_text_button, this, true)
        orientation = HORIZONTAL
        isClickable = true
        isFocusable = true

        setBackgroundColor(android.graphics.Color.TRANSPARENT)

        leftIcon = findViewById(R.id.leftIcon)
        rightIcon = findViewById(R.id.rightIcon)
        textView = findViewById(R.id.buttonText)

        // XML 속성 처리
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TextButtonView, 0, 0)
            try {
                val text = typedArray.getString(R.styleable.TextButtonView_tb_text)
                val leftIconResId = typedArray.getResourceId(R.styleable.TextButtonView_tb_leftIcon, 0)
                val rightIconResId = typedArray.getResourceId(R.styleable.TextButtonView_tb_rightIcon, 0)

                text?.let { setText(it) }
                setLeftIcon(if (leftIconResId != 0) leftIconResId else null)
                setRightIcon(if (rightIconResId != 0) rightIconResId else null)
            } finally {
                typedArray.recycle()
            }
        }

    }

    fun setText(text: String) {
        textView.text = text
    }

    fun setLeftIcon(@DrawableRes resId: Int?) {
        if (resId != null) {
            leftIcon.setImageResource(resId)
            leftIcon.visibility = VISIBLE
        } else {
            leftIcon.visibility = GONE
        }
    }

    fun setRightIcon(@DrawableRes resId: Int?) {
        if (resId != null) {
            rightIcon.setImageResource(resId)
            rightIcon.visibility = VISIBLE
        } else {
            rightIcon.visibility = GONE
        }
    }

//    // 버튼 활성화/비활성화 상태 설정
//    fun setButtonEnabled(enabled: Boolean) {
//        isEnabled = enabled
//        isClickable = enabled
//        isFocusable = enabled
//    }

    // 각 뷰에 직접 접근할 수 있도록 public 메서드 제공
    fun getTextView(): TextView = textView
    fun getLeftIconView(): ImageView = leftIcon
    fun getRightIconView(): ImageView = rightIcon
}