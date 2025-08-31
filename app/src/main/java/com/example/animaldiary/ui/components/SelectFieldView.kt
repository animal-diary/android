package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.animaldiary.R

class SelectFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val titleText: TextView
    private val requiredMark: TextView
    private val descriptionText: TextView
    private val inputContainer: LinearLayout
    private val valueText: TextView
    private val icon: ImageView
    private val errorText: TextView

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.component_select_field, this, true)

        titleText = findViewById(R.id.sf_title)
        requiredMark = findViewById(R.id.sf_requiredMark)
        descriptionText = findViewById(R.id.sf_description)
        inputContainer = findViewById(R.id.sf_inputContainer)
        valueText = findViewById(R.id.sf_value)
        icon = findViewById(R.id.sf_icon)
        errorText = findViewById(R.id.sf_errorText)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.SelectFieldView)
            try {
                // 속성 읽기
                val title = typedArray.getString(R.styleable.SelectFieldView_sf_title)
                val isRequired = typedArray.getBoolean(R.styleable.SelectFieldView_sf_required, false)
                val description = typedArray.getString(R.styleable.SelectFieldView_sf_description)
                val placeholder = typedArray.getString(R.styleable.SelectFieldView_sf_placeholder)
                val iconResId = typedArray.getResourceId(R.styleable.SelectFieldView_sf_icon, 0)
                val error = typedArray.getString(R.styleable.SelectFieldView_sf_errorText)
                val showTitleAttr = typedArray.getBoolean(R.styleable.SelectFieldView_sf_showTitle, true)
                val showDescriptionAttr = typedArray.getBoolean(R.styleable.SelectFieldView_sf_showDescription, true)

                // 속성을 기반으로 초기 상태 적용
                setTitle(title, showTitleAttr)
                setDescription(description, showDescriptionAttr)
                showRequiredMark(isRequired)
                setPlaceholder(placeholder)
                if (iconResId != 0) {
                    setIcon(iconResId)
                }
                setErrorText(error)

                when (typedArray.getInt(R.styleable.SelectFieldView_sf_state, 0)) {
                    0 -> setNormalState()
                    1 -> setFocusedState()
                    2 -> setErrorState()
                    3 -> setDisabledState()
                }

            } finally {
                typedArray.recycle()
            }
        }
    }

    // 동적 제어를 위한 공개 함수들
    fun setTitle(title: String?, show: Boolean = true) {
        titleText.text = title
        titleText.isVisible = show
    }

    fun setDescription(description: String?, show: Boolean = true) {
        descriptionText.text = description
        descriptionText.isVisible = show
    }

    fun showRequiredMark(show: Boolean) {
        requiredMark.isVisible = show
    }

    fun setPlaceholder(placeholder: String?) {
        valueText.hint = placeholder
    }

    fun setIcon(iconResId: Int) {
        icon.setImageResource(iconResId)
        icon.isVisible = true
    }

    fun showIcon(show: Boolean) {
        icon.isVisible = show
    }

    fun setErrorText(error: String?) {
        errorText.text = error
        errorText.isVisible = !error.isNullOrBlank()
    }

    fun setValue(value: String) {
        valueText.text = value
    }

    fun getValue(): String {
        return valueText.text.toString()
    }

    fun setOnClickListenerForInput(listener: () -> Unit) {
        inputContainer.setOnClickListener { listener() }
    }

    // 상태
    fun setNormalState() {
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_default)
        errorText.visibility = View.GONE
        valueText.isEnabled = true
        icon.isEnabled = true
    }

    fun setFocusedState() {
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_active)
        errorText.visibility = View.GONE
    }

    fun setErrorState() {
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_error)
        errorText.visibility = View.VISIBLE
    }

    fun setDisabledState() {
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_disabled)
        valueText.isEnabled = false
        icon.isEnabled = false
    }
}