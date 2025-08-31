package com.example.animaldiary.ui.components

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
    private var showTitle: Boolean = true
    private var showDescription: Boolean = true
    private var isRequired: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.component_text_area, this, true)
        orientation = VERTICAL

        // 뷰 찾기
        titleContainer = findViewById(R.id.ta_titleContainer)
        tvTitle = findViewById(R.id.ta_title)
        tvRequired = findViewById(R.id.ta_requiredMark)
        tvDesc = findViewById(R.id.ta_description)
        etInput = findViewById(R.id.ta_input)
        tvCount = findViewById(R.id.ta_count)

        // XML 속성 처리
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.TextAreaView, 0, 0)
            try {
                // 속성 읽기 및 적용
                ta.getString(R.styleable.TextAreaView_ta_title)?.let { setTitle(it) }
                isRequired = ta.getBoolean(R.styleable.TextAreaView_ta_required, false)
                ta.getString(R.styleable.TextAreaView_ta_description)?.let { setDescription(it) }
                showCount = ta.getBoolean(R.styleable.TextAreaView_ta_showCount, false)
                maxLength = ta.getInt(R.styleable.TextAreaView_ta_maxLength, 200)
                showTitle = ta.getBoolean(R.styleable.TextAreaView_ta_showTitle, true)
                showDescription = ta.getBoolean(R.styleable.TextAreaView_ta_showDescription, true)

                // 속성값에 따라 초기 상태 설정
                when (ta.getInt(R.styleable.TextAreaView_ta_state, 0)) {
                    0 -> setDefaultState()
                    1 -> setFocusedState()
                    2 -> setDisabledState()
                }

            } finally {
                ta.recycle()
            }
        }

        // 포커스 상태 처리
        etInput.setOnFocusChangeListener { _, hasFocus ->
            val text = etInput.text.toString()
            when {
                !isEnabled -> setDisabledState()
                hasFocus -> setFocusedState()
                text.isNotBlank() -> setFocusedState()
                else -> setDefaultState()
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

        // 초기 가시성 설정
        titleContainer.visibility = if (showTitle) View.VISIBLE else View.GONE
        tvRequired.visibility = if (isRequired) View.VISIBLE else View.GONE
        tvDesc.visibility = if (showDescription) View.VISIBLE else View.GONE
        tvCount.visibility = if (showCount) View.VISIBLE else View.GONE
        setDefaultState()
    }

    fun setTitle(text: String) {
        tvTitle.text = text
        titleContainer.visibility = View.VISIBLE
    }

    fun showTitle(show: Boolean) {
        titleContainer.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showRequired(show: Boolean) {
        tvRequired.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setDescription(desc: String) {
        tvDesc.text = desc
        tvDesc.visibility = View.VISIBLE
    }

    fun showDescription(show: Boolean) {
        tvDesc.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showCount(show: Boolean) {
        showCount = show
        tvCount.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setMaxLength(length: Int) {
        maxLength = length
        if (showCount) {
            val len = etInput.text?.length ?: 0
            tvCount.text = "$len/$maxLength"
        }
    }

    fun getText(): String = etInput.text.toString()

    // 상태별 배경 변경
    fun setDefaultState() {
        etInput.background = ContextCompat.getDrawable(context, R.drawable.bg_text_input_default)
    }
    fun setFocusedState() {
        etInput.background = ContextCompat.getDrawable(context, R.drawable.bg_text_input_active)
    }
    fun setDisabledState() {
        etInput.isEnabled = false
        etInput.background = ContextCompat.getDrawable(context, R.drawable.bg_text_input_disabled)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}