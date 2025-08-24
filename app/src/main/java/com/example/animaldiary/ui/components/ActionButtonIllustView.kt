package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.animaldiary.R

class ActionButtonIllustView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val illustIcon: ImageView
    private val buttonText: TextView

    init {
        // 기본 레이아웃 속성 설정
        orientation = VERTICAL
        gravity = android.view.Gravity.CENTER_HORIZONTAL

        // 레이아웃 인플레이션
        LayoutInflater.from(context).inflate(R.layout.component_action_button_illust, this, true)

        // 뷰 참조 초기화
        illustIcon = findViewById(R.id.illustIcon)
        buttonText = findViewById(R.id.buttonText)

        // attrs 처리
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ActionButtonIllustView, 0, 0)

            try {
                // abi_text 속성 처리
                val text = typedArray.getString(R.styleable.ActionButtonIllustView_abi_text)
                buttonText.text = text

                // abi_illust 속성 처리
                val illustResId = typedArray.getResourceId(R.styleable.ActionButtonIllustView_abi_illust, 0)
                if (illustResId != 0) {
                    illustIcon.setImageResource(illustResId)
                }

                // abi_state 속성 처리 (기본값은 normal)
                val state = typedArray.getInt(R.styleable.ActionButtonIllustView_abi_state, 0)
                updateButtonState(state)

            } finally {
                typedArray.recycle()
            }
        }
    }

    private fun updateButtonState(state: Int) {
        when (state) {
            0 -> applyButtonStyles(
                R.drawable.bg_action_button_illust_default,
                R.color.fg_neutral_primary,
                true
            )
            1 -> applyButtonStyles(
                R.drawable.bg_action_button_illust_pressed,
                R.color.fg_neutral_primary,
                true
            )
            2 -> applyButtonStyles(
                R.drawable.bg_action_button_illust_disabled,
                R.color.fg_disabled,
                false
            )
        }
    }

    private fun applyButtonStyles(backgroundResId: Int, textColorResId: Int, isEnabled: Boolean) {
        // 배경 설정
        background = context.getDrawable(backgroundResId)

        // 텍스트 및 일러스트 색상 설정
        val color = context.getColor(textColorResId)
        buttonText.setTextColor(color)
        illustIcon.setColorFilter(color)

        // 버튼 활성화 상태 설정
        this.isEnabled = isEnabled
    }
}