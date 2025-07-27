package com.example.animaldiary.components

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.animaldiary.R

class TextAreaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val titleContainer: View
    private val tvTitle: TextView
    private val tvRequired: TextView
    private val tvDesc: TextView
    private val etInput: EditText
    private val tvCount: TextView

    private var maxLength: Int = 200
    private var showCount: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.component_text_area, this, true)
        orientation = VERTICAL

        // find views
        titleContainer = findViewById(R.id.ta_titleContainer)
        tvTitle = findViewById(R.id.ta_title)
        tvRequired = findViewById(R.id.ta_requiredMark)
        tvDesc = findViewById(R.id.ta_description)
        etInput = findViewById(R.id.ta_input)
        tvCount = findViewById(R.id.ta_count)

        // click focus
        etInput.setOnFocusChangeListener { _, hasFocus ->
            val text = etInput.text.toString()
            when {
                !isEnabled -> setStateDisabled()
                hasFocus -> setStateActive()
                text.isNotBlank() -> setStateCompleted()
                else -> setStateDefault()
            }
        }
        etInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (showCount) {
                    val len = s?.length ?: 0
                    tvCount.text = "$len/$maxLength"
                }
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) = Unit
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) = Unit
        })

        // 초기 visibility & state
        titleContainer.visibility = View.GONE
        tvRequired.visibility = View.GONE
        tvDesc.visibility = View.GONE
        tvCount.visibility = View.GONE
        setStateDefault()
    }

    fun setTitle(text: String) {
        tvTitle.text = text
        titleContainer.visibility = View.VISIBLE
    }

    fun showRequired(show: Boolean) {
        tvRequired.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setDescription(desc: String) {
        tvDesc.text = desc
        tvDesc.visibility = View.VISIBLE
    }

    fun showCount(show: Boolean) {
        showCount = show
        tvCount.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setMaxLength(length: Int) {
        maxLength = length
    }

    fun getText(): String = etInput.text.toString()

    // 상태별 배경 변경
    private fun setStateDefault() {
        etInput.background = ContextCompat.getDrawable(context, R.drawable.bg_text_area_default)
    }
    private fun setStateActive() {
        etInput.background = ContextCompat.getDrawable(context, R.drawable.bg_text_area_active)
    }
    private fun setStateCompleted() {
        etInput.background = ContextCompat.getDrawable(context, R.drawable.bg_text_area_default)
    }
    private fun setStateDisabled() {
        etInput.isEnabled = false
//        etInput.background = ContextCompat.getDrawable(context, R.drawable.bg_text_area_disabled)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
