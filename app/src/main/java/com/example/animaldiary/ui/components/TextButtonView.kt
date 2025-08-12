package com.example.animaldiary.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.example.animaldiary.R

class TextButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val leftIcon: ImageView
    private val rightIcon: ImageView
    private val textView: TextView

    // State와 Hierarchy
    private var currentState: ButtonState = ButtonState.DEFAULT
    private var hierarchy: ButtonHierarchy = ButtonHierarchy.PRIMARY

    // 색상 맵
    private val textColors = mutableMapOf<Pair<ButtonHierarchy, ButtonState>, Int>()
    private val iconColors = mutableMapOf<Pair<ButtonHierarchy, ButtonState>, Int>()

    enum class ButtonState(val value: Int) {
        DEFAULT(0), PRESSED(1), DISABLED(2)
    }

    enum class ButtonHierarchy(val value: Int) {
        PRIMARY(0), NEUTRAL(1)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.component_text_button, this, true)
        orientation = HORIZONTAL
        isClickable = true
        isFocusable = true
        setBackgroundColor(android.graphics.Color.TRANSPARENT)

        leftIcon = findViewById(R.id.leftIcon)
        rightIcon = findViewById(R.id.rightIcon)
        textView = findViewById(R.id.buttonText)

        initializeDefaultColors()
        setupTouchListener()

        // XML 속성 처리
        attrs?.let { processAttributes(it) }

        // 초기 상태 적용
        updateAppearance()
    }

    private fun initializeDefaultColors() {
        // Primary 색상
        textColors[Pair(ButtonHierarchy.PRIMARY, ButtonState.DEFAULT)] =
            ContextCompat.getColor(context, R.color.fg_brand_default)
        textColors[Pair(ButtonHierarchy.PRIMARY, ButtonState.PRESSED)] =
            ContextCompat.getColor(context, R.color.fg_brand_pressed)
        textColors[Pair(ButtonHierarchy.PRIMARY, ButtonState.DISABLED)] =
            ContextCompat.getColor(context, R.color.fg_disabled)

        // Neutral 색상
        textColors[Pair(ButtonHierarchy.NEUTRAL, ButtonState.DEFAULT)] =
            ContextCompat.getColor(context, R.color.fg_neutral_tertiary)
        textColors[Pair(ButtonHierarchy.NEUTRAL, ButtonState.PRESSED)] =
            ContextCompat.getColor(context, R.color.fg_neutral_tertiary)
        textColors[Pair(ButtonHierarchy.NEUTRAL, ButtonState.DISABLED)] =
            ContextCompat.getColor(context, R.color.fg_disabled)

        // 아이콘 색상 (텍스트와 동일)
        iconColors.putAll(textColors)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener() {
        setOnTouchListener { _, event ->
            if (currentState == ButtonState.DISABLED) return@setOnTouchListener false

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    currentState = ButtonState.PRESSED
                    updateAppearance()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    currentState = ButtonState.DEFAULT
                    updateAppearance()
                }
            }
            false // onClick 이벤트 유지
        }
    }

    private fun processAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextButtonView, 0, 0)
        try {
            // 기본 속성
            val text = typedArray.getString(R.styleable.TextButtonView_tb_text)
            val leftIconResId = typedArray.getResourceId(R.styleable.TextButtonView_tb_leftIcon, 0)
            val rightIconResId = typedArray.getResourceId(R.styleable.TextButtonView_tb_rightIcon, 0)

            // 새로운 속성들
            val stateValue = typedArray.getInt(R.styleable.TextButtonView_tb_state, 0)
            val hierarchyValue = typedArray.getInt(R.styleable.TextButtonView_tb_hierarchy, 0)

            // 값 적용
            text?.let { setText(it) }
            setLeftIcon(if (leftIconResId != 0) leftIconResId else null)
            setRightIcon(if (rightIconResId != 0) rightIconResId else null)

            currentState = ButtonState.values().find { it.value == stateValue } ?: ButtonState.DEFAULT
            hierarchy = ButtonHierarchy.values().find { it.value == hierarchyValue } ?: ButtonHierarchy.PRIMARY

        } finally {
            typedArray.recycle()
        }
    }

    private fun updateAppearance() {
        val key = Pair(hierarchy, currentState)

        // 텍스트 색상 적용
        textColors[key]?.let { color ->
            textView.setTextColor(color)
        }

        // 아이콘 색상 적용
        iconColors[key]?.let { color ->
            val colorStateList = ColorStateList.valueOf(color)
            if (leftIcon.visibility == VISIBLE) {
                ImageViewCompat.setImageTintList(leftIcon, colorStateList)
            }
            if (rightIcon.visibility == VISIBLE) {
                ImageViewCompat.setImageTintList(rightIcon, colorStateList)
            }
        }

        // 클릭 가능 상태 업데이트
        isClickable = currentState != ButtonState.DISABLED
        isFocusable = currentState != ButtonState.DISABLED
    }

    // Public API
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
        updateAppearance() // 아이콘 변경 시 색상도 다시 적용
    }

    fun setRightIcon(@DrawableRes resId: Int?) {
        if (resId != null) {
            rightIcon.setImageResource(resId)
            rightIcon.visibility = VISIBLE
        } else {
            rightIcon.visibility = GONE
        }
        updateAppearance() // 아이콘 변경 시 색상도 다시 적용
    }

    fun setState(state: ButtonState) {
        if (currentState != state) {
            currentState = state
            updateAppearance()
        }
    }

    fun setHierarchy(hierarchy: ButtonHierarchy) {
        if (this.hierarchy != hierarchy) {
            this.hierarchy = hierarchy
            updateAppearance()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        setState(if (enabled) ButtonState.DEFAULT else ButtonState.DISABLED)
    }

    // 기존 호환성을 위한 메서드들
    fun getTextView(): TextView = textView
    fun getLeftIconView(): ImageView = leftIcon
    fun getRightIconView(): ImageView = rightIcon
}