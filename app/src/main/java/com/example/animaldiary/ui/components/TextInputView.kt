package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.animaldiary.R

class TextInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val title: TextView
    private val requiredMark: TextView
    private val description: TextView
    private val input: EditText
    private val icon: ImageView
    private val errorText: TextView
    private val inputContainer: LinearLayout

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.component_text_input, this, true)

        title = findViewById(R.id.ti_title)
        requiredMark = findViewById(R.id.ti_requiredMark)
        description = findViewById(R.id.ti_description)
        input = findViewById(R.id.ti_input)
        icon = findViewById(R.id.ti_icon)
        errorText = findViewById(R.id.ti_errorText)
        inputContainer = findViewById(R.id.ti_inputContainer)

        // XML 속성 처리
        attrs?.let {
            val ti = context.obtainStyledAttributes(it, R.styleable.TextInputView, 0, 0)
            try {
                // ti_title 속성
                val tiTitle = ti.getString(R.styleable.TextInputView_ti_title)
                val showTitle = ti.getBoolean(R.styleable.TextInputView_ti_showTitle, true)
                if (showTitle) {
                    title.text = tiTitle
                }
                showTitle(showTitle)

                // ti_required 속성
                val isRequired = ti.getBoolean(R.styleable.TextInputView_ti_required, false)
                showRequiredMark(isRequired)

                // ti_description 속성
                val tiDescription = ti.getString(R.styleable.TextInputView_ti_description)
                val showDescription = ti.getBoolean(R.styleable.TextInputView_ti_showDescription, true)
                if (showDescription) {
                    this.description.text = tiDescription
                }
                showDescription(showDescription)

                // ti_placeholder 속성
                ti.getString(R.styleable.TextInputView_ti_placeholder)?.let {
                    input.hint = it
                }

                // ti_icon 속성
                val iconRes = ti.getResourceId(R.styleable.TextInputView_ti_icon, 0)
                if (iconRes != 0) {
                    icon.setImageResource(iconRes)
                    showIcon(true)
                } else {
                    showIcon(false)
                }

                // ti_errorText 속성
                ti.getString(R.styleable.TextInputView_ti_errorText)?.let {
                    errorText.text = it
                }

                when (ti.getInt(R.styleable.TextInputView_ti_state, 0)) {
                    0 -> setDefaultState()
                    1 -> setFocusedState()
                    2 -> setErrorState()
                    3 -> setDisabledState()
                }
            } finally {
                ti.recycle()
            }
        }

        input.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setFocusedState()
            } else {
                setDefaultState()
            }
        }
    }

    // 동적 제어를 위한 공개 함수들
    fun setTitle(text: String) {
        title.text = text
    }

    fun showTitle(show: Boolean) {
        title.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showRequiredMark(show: Boolean) {
        requiredMark.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setDescription(text: String) {
        description.text = text
    }

    fun showDescription(show: Boolean) {
        description.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showIcon(show: Boolean) {
        icon.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun getText(): String = input.text.toString()

    fun setError(message: String) {
        errorText.text = message
        setErrorState()
    }

    fun clearError() {
        errorText.visibility = View.GONE
        setDefaultState()
    }

    fun setDefaultState() {
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_default)
        errorText.visibility = View.GONE
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
        input.isEnabled = false
        input.setTextColor(ContextCompat.getColor(context, R.color.fg_disabled))
        input.setHintTextColor(ContextCompat.getColor(context, R.color.fg_disabled))
        icon.imageAlpha = 100
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_disabled)
    }
}