package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.animaldiary.R

enum class DatePickerItemType {
    CHIP,  // 양력/음력
    DATE   //년/월/일
}

class DatePickerItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val containerView: LinearLayout
    private val textView: TextView

    private var isSelectedState: Boolean = false
    private var itemType: DatePickerItemType = DatePickerItemType.CHIP
    private var onItemClickListener: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.component_date_picker_item, this, true)

        containerView = findViewById(R.id.dpi_container)
        textView = findViewById(R.id.dpi_text)

        // XML 속성 처리
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.DatePickerItemView, 0, 0)
            try {
                val text = typedArray.getString(R.styleable.DatePickerItemView_dpi_text)
                val selected = typedArray.getBoolean(R.styleable.DatePickerItemView_dpi_selected, false)

                text?.let { setText(it) }
                setItemSelected(selected)
            } finally {
                typedArray.recycle()
            }
        }

        containerView.setOnClickListener {
            onItemClickListener?.invoke()
        }

        // 초기 상태 설정
        updateState()
    }

    fun setText(text: String) {
        textView.text = text
    }

    fun getText(): String {
        return textView.text.toString()
    }

    fun setItemSelected(selected: Boolean) {
        isSelectedState = selected
        updateState()
    }

    fun isItemSelected(): Boolean {
        return isSelectedState
    }

    fun setItemType(type: DatePickerItemType) {
        itemType = type
        updateState()
    }

    fun setOnItemClickListener(listener: () -> Unit) {
        onItemClickListener = listener
    }

    private fun updateState() {
        when (itemType) {
            DatePickerItemType.CHIP -> {
                // 양력/음력
                textView.setTextAppearance(R.style.Text_Body14Strong)
                if (isSelectedState) {
                    containerView.setBackgroundResource(R.drawable.bg_date_picker_item_selected)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.fg_brand_default))
                } else {
                    containerView.setBackgroundResource(R.drawable.bg_date_picker_item_unselected)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.fg_neutral_primary))
                }
            }
            DatePickerItemType.DATE -> {
                // 년/월/일
                textView.setTextAppearance(R.style.Text_Title24Strong)
                if (isSelectedState) {
                    containerView.setBackgroundResource(R.drawable.bg_date_picker_selected)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.fg_brand_default))
                } else {
                    containerView.setBackgroundResource(R.drawable.bg_date_picker_unselected)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.fg_neutral_primary))
                }
            }
        }
    }
}