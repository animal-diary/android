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
                // Title
                val title = typedArray.getString(R.styleable.SelectFieldView_sf_title)
                titleText.text = title ?: ""
                titleText.isVisible = !title.isNullOrBlank()

                // Required
                val isRequired = typedArray.getBoolean(R.styleable.SelectFieldView_sf_required, false)
                requiredMark.visibility = if (isRequired) View.VISIBLE else View.GONE

                // Description
                val description = typedArray.getString(R.styleable.SelectFieldView_sf_description)
                descriptionText.text = description ?: ""
                descriptionText.isVisible = !description.isNullOrBlank()

                // Placeholder
                val placeholder = typedArray.getString(R.styleable.SelectFieldView_sf_placeholder)
                valueText.hint = placeholder ?: ""

                // Icon
                val iconResId = typedArray.getResourceId(R.styleable.SelectFieldView_sf_icon, 0)
                if (iconResId != 0) {
                    icon.setImageResource(iconResId)
                    icon.visibility = View.VISIBLE
                } else {
                    icon.visibility = View.GONE
                }

                // Error Text
                val error = typedArray.getString(R.styleable.SelectFieldView_sf_errorText)
                errorText.text = error ?: ""
                errorText.isVisible = !error.isNullOrBlank()

                // State
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

    // States
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

    fun setValue(value: String) {
        valueText.text = value
    }

    fun getValue(): String {
        return valueText.text.toString()
    }

    fun setOnClickListenerForInput(listener: () -> Unit) {
        valueText.setOnClickListener { listener() }
        icon.setOnClickListener { listener() }
    }
}
