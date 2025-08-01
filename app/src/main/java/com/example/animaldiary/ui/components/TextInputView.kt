package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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

        // 기본 숨김 처리
        title.visibility = GONE
        requiredMark.visibility = GONE
        description.visibility = GONE
        errorText.visibility = GONE
        icon.visibility = GONE

        // XML 속성 처리
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.TextInputView, 0, 0)
            try {
                a.getString(R.styleable.TextInputView_ti_title)?.let {
                    title.text = it
                    title.visibility = VISIBLE
                }
                a.getBoolean(R.styleable.TextInputView_ti_required, false).let {
                    requiredMark.visibility = if (it) VISIBLE else GONE
                }
                a.getString(R.styleable.TextInputView_ti_description)?.let {
                    description.text = it
                    description.visibility = VISIBLE
                }
                a.getString(R.styleable.TextInputView_ti_placeholder)?.let {
                    input.hint = it
                }
                val iconRes = a.getResourceId(R.styleable.TextInputView_ti_icon, 0)
                if (iconRes != 0) {
                    icon.setImageResource(iconRes)
                    icon.visibility = VISIBLE
                }
                a.getString(R.styleable.TextInputView_ti_errorText)?.let {
                    errorText.text = it
                }

                when (a.getInt(R.styleable.TextInputView_ti_state, 0)) {
                    0 -> setDefaultState()
                    1 -> setFocusedState()
                    2 -> setErrorState()
                    3 -> setDisabledState()
                }
            } finally {
                a.recycle()
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

    fun getText(): String = input.text.toString()

    fun setError(message: String) {
        errorText.text = message
        errorText.visibility = VISIBLE
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_error)
    }

    fun clearError() {
        errorText.visibility = GONE
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_default)
    }

    fun setDefaultState() {
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_default)
        errorText.visibility = GONE
    }

    fun setFocusedState() {
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_active)
        errorText.visibility = GONE
    }

    fun setErrorState() {
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_error)
        errorText.visibility = VISIBLE
    }

    private fun setDisabledState() {
        input.isEnabled = false
        input.setTextColor(ContextCompat.getColor(context, R.color.fg_disabled))
        input.setHintTextColor(ContextCompat.getColor(context, R.color.fg_disabled))
        icon.imageAlpha = 100
        inputContainer.setBackgroundResource(R.drawable.bg_text_input_disabled)
    }
}
