package com.example.animaldiary.components

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

        leftIcon = findViewById(R.id.leftIcon)
        rightIcon = findViewById(R.id.rightIcon)
        textView = findViewById(R.id.buttonText)
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
}
