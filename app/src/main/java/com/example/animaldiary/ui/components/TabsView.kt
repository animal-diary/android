package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.animaldiary.R

class TabsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val tabText: TextView
    private val tabIndicator: View

    init {
        LayoutInflater.from(context).inflate(R.layout.component_tabs, this, true)

        tabText = findViewById(R.id.tabText)
        tabIndicator = findViewById(R.id.tabIndicator)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TabsView)
            try {
                // tv_title
                val title = typedArray.getString(R.styleable.TabsView_tv_title)
                tabText.text = title ?: ""

                // tv_state
                val state = typedArray.getInt(R.styleable.TabsView_tv_state, 0)
                updateTabState(state)
            } finally {
                typedArray.recycle()
            }
        }
    }

    // 탭 상태 업데이트 메서드 (normal, pressed)
    private fun updateTabState(state: Int) {
        when (state) {
            0 -> { // normal
                tabText.setTextColor(ContextCompat.getColor(context, R.color.fg_neutral_tertiary))
                tabIndicator.background = ContextCompat.getDrawable(context, R.color.fg_neutral_assistive)
            }
            1 -> { // pressed
                tabText.setTextColor(ContextCompat.getColor(context, R.color.fg_brand_pressed))
                tabIndicator.background = ContextCompat.getDrawable(context, R.color.fg_brand_pressed)
            }
        }
    }

    // 외부에서 탭 텍스트를 설정할 수 있는 공개 메서드
    fun setTabTitle(title: String) {
        tabText.text = title
    }

    // 외부에서 탭 선택 상태를 제어할 수 있는 공개 메서드
    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            tabText.setTextColor(ContextCompat.getColor(context, R.color.fg_selected))
            tabIndicator.background = ContextCompat.getDrawable(context, R.color.outline_selected)
        } else {
            tabText.setTextColor(ContextCompat.getColor(context, R.color.fg_neutral_tertiary))
            tabIndicator.background = ContextCompat.getDrawable(context, R.color.fg_neutral_assistive)
        }
    }
}