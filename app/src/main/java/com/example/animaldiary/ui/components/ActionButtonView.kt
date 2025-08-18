package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.animaldiary.R

class ActionButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val buttonContainer: LinearLayout
    private val leftIcon: ImageView
    private val buttonText: TextView
    private val rightIcon: ImageView
    private var buttonHeight: Int = 0

    var ab_text: String?
        get() = buttonText.text.toString()
        set(value) {
            buttonText.text = value
        }

    init {
        // 기본값들을 명시적으로 설정
        setPadding(0, 0, 0, 0)

        // 레이아웃 파라미터 설정
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, 0)
        }

        // 레이아웃 인플레이션
        val inflatedView = LayoutInflater.from(context).inflate(R.layout.component_action_button, this, false)

        // inflate된 뷰를 직접 추가하지 않고, 그 속성들을 현재 뷰에 적용
        buttonContainer = inflatedView as LinearLayout

        // buttonContainer의 모든 자식 뷰를 현재 뷰로 이동
        while (buttonContainer.childCount > 0) {
            val child = buttonContainer.getChildAt(0)
            buttonContainer.removeView(child)
            addView(child)
        }

        // buttonContainer의 속성들을 현재 뷰에 복사
        orientation = buttonContainer.orientation
        gravity = buttonContainer.gravity

        // 뷰 참조 초기화
        leftIcon = findViewById(R.id.leftIcon)
        buttonText = findViewById(R.id.buttonText)
        rightIcon = findViewById(R.id.rightIcon)

        // 기본 높이 설정
        buttonHeight = resources.getDimensionPixelSize(R.dimen.action_button_height_md)

        // attrs 처리
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ActionButtonView, 0, 0)

            try {
                val text = typedArray.getString(R.styleable.ActionButtonView_ab_text)
                buttonText.text = text

                val leftIconResId = typedArray.getResourceId(R.styleable.ActionButtonView_ab_leftIcon, 0)
                val rightIconResId = typedArray.getResourceId(R.styleable.ActionButtonView_ab_rightIcon, 0)

                val isLeftIconVisible = typedArray.getBoolean(R.styleable.ActionButtonView_ab_leftIconVisible, false)
                val isRightIconVisible = typedArray.getBoolean(R.styleable.ActionButtonView_ab_rightIconVisible, false)

                val size = typedArray.getInt(R.styleable.ActionButtonView_ab_size, 1)
                val hierarchy = typedArray.getInt(R.styleable.ActionButtonView_ab_hierarchy, 0)
                val type = typedArray.getInt(R.styleable.ActionButtonView_ab_type, 0)
                val state = typedArray.getInt(R.styleable.ActionButtonView_ab_state, 0)

                // 먼저 크기 설정
                updateButtonSize(size)

                updateTextStyle(size)

                // 그 다음 아이콘과 패딩 설정
                updateIconsAndPadding(leftIconResId, rightIconResId, isLeftIconVisible, isRightIconVisible, size)

                // 마지막으로 버튼 상태 설정
                updateButtonState(hierarchy, type, state)

            } finally {
                typedArray.recycle()
            }
        }
    }

    // 프로그래매틱하게 버튼 스타일을 업데이트하는 메서드 추가
    fun updateButtonProgrammatically(
        text: String,
        size: Int,
        hierarchy: Int,
        type: Int,
        state: Int,
        leftIconResId: Int = 0,
        rightIconResId: Int = 0,
        isLeftIconVisible: Boolean = false,
        isRightIconVisible: Boolean = false
    ) {
        buttonText.text = text

        // 먼저 크기 설정
        updateButtonSize(size)
        updateTextStyle(size)

        // 그 다음 아이콘과 패딩 설정
        updateIconsAndPadding(leftIconResId, rightIconResId, isLeftIconVisible, isRightIconVisible, size)

        // 마지막으로 버튼 상태 설정
        updateButtonState(hierarchy, type, state)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 높이를 고정값으로 설정
        val fixedHeightSpec = MeasureSpec.makeMeasureSpec(buttonHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, fixedHeightSpec)
    }

    private fun updateTextStyle(size: Int) {
        when (size) {
            0 -> buttonText.setTextAppearance(context, R.style.Text_ButtonSm)
            1 -> buttonText.setTextAppearance(context, R.style.Text_ButtonMd)
            2 -> buttonText.setTextAppearance(context, R.style.Text_ButtonLg)
        }

        buttonText.gravity = Gravity.CENTER
        val textParams = buttonText.layoutParams as? LinearLayout.LayoutParams
        textParams?.weight = 1f  // 남은 공간을 모두 차지
        buttonText.layoutParams = textParams
    }

    private fun updateButtonSize(size: Int) {
        buttonHeight = when (size) {
            0 -> resources.getDimensionPixelSize(R.dimen.action_button_height_sm) // 32dp
            1 -> resources.getDimensionPixelSize(R.dimen.action_button_height_md) // 48dp
            2 -> resources.getDimensionPixelSize(R.dimen.action_button_height_lg) // 56dp
            else -> resources.getDimensionPixelSize(R.dimen.action_button_height_md)
        }

        // 레이아웃 다시 측정하도록 요청
        requestLayout()
    }

    private fun updateIconsAndPadding(
        leftIconResId: Int,
        rightIconResId: Int,
        isLeftIconVisible: Boolean,
        isRightIconVisible: Boolean,
        size: Int
    ) {
        // 아이콘 리소스 설정
        if (leftIconResId != 0) {
            leftIcon.setImageResource(leftIconResId)
        }
        if (rightIconResId != 0) {
            rightIcon.setImageResource(rightIconResId)
        }

        // 아이콘 가시성 설정
        leftIcon.isVisible = isLeftIconVisible
        rightIcon.isVisible = isRightIconVisible

        // 아이콘 크기 설정 (사이즈에 따라)
        val iconSize = when (size) {
            0 -> resources.getDimensionPixelSize(R.dimen.icon_size_sm) // 16dp
            1 -> resources.getDimensionPixelSize(R.dimen.icon_size_md) // 20dp
            2 -> resources.getDimensionPixelSize(R.dimen.icon_size_lg) // 24dp
            else -> resources.getDimensionPixelSize(R.dimen.icon_size_md)
        }

        // 아이콘 크기 적용
        val leftIconParams = leftIcon.layoutParams
        leftIconParams.width = iconSize
        leftIconParams.height = iconSize
        leftIcon.layoutParams = leftIconParams

        val rightIconParams = rightIcon.layoutParams
        rightIconParams.width = iconSize
        rightIconParams.height = iconSize
        rightIcon.layoutParams = rightIconParams

        // 패딩 설정
        val (startPadding, endPadding) = calculatePadding(size, isLeftIconVisible, isRightIconVisible)
        setPadding(startPadding, paddingTop, endPadding, paddingBottom)

        // 아이콘 마진 설정
        setIconMargins(size, isLeftIconVisible, isRightIconVisible)
    }

    private fun calculatePadding(size: Int, hasLeftIcon: Boolean, hasRightIcon: Boolean): Pair<Int, Int> {
        return when (size) {
            0 -> { // sm (32dp 높이)
                when {
                    !hasLeftIcon && !hasRightIcon -> resources.getDimensionPixelSize(R.dimen.padding_lg) to resources.getDimensionPixelSize(R.dimen.padding_lg)
                    hasLeftIcon && !hasRightIcon -> resources.getDimensionPixelSize(R.dimen.spacing_sm) to resources.getDimensionPixelSize(R.dimen.padding_md)
                    !hasLeftIcon && hasRightIcon -> resources.getDimensionPixelSize(R.dimen.padding_md) to resources.getDimensionPixelSize(R.dimen.spacing_sm)
                    else -> resources.getDimensionPixelSize(R.dimen.spacing_md) to resources.getDimensionPixelSize(R.dimen.spacing_md)
                }
            }
            1 -> { // md (48dp 높이)
                when {
                    !hasLeftIcon && !hasRightIcon -> resources.getDimensionPixelSize(R.dimen.padding_xl) to resources.getDimensionPixelSize(R.dimen.padding_xl)
                    hasLeftIcon && !hasRightIcon -> resources.getDimensionPixelSize(R.dimen.spacing_md) to resources.getDimensionPixelSize(R.dimen.padding_lg)
                    !hasLeftIcon && hasRightIcon -> resources.getDimensionPixelSize(R.dimen.padding_lg) to resources.getDimensionPixelSize(R.dimen.spacing_md)
                    else -> resources.getDimensionPixelSize(R.dimen.spacing_lg) to resources.getDimensionPixelSize(R.dimen.spacing_lg)
                }
            }
            2 -> { // lg (56dp 높이)
                when {
                    !hasLeftIcon && !hasRightIcon -> resources.getDimensionPixelSize(R.dimen.padding_3xl) to resources.getDimensionPixelSize(R.dimen.padding_3xl)
                    hasLeftIcon && !hasRightIcon -> resources.getDimensionPixelSize(R.dimen.padding_xl) to resources.getDimensionPixelSize(R.dimen.padding_2xl)
                    !hasLeftIcon && hasRightIcon -> resources.getDimensionPixelSize(R.dimen.padding_2xl) to resources.getDimensionPixelSize(R.dimen.padding_xl)
                    else -> resources.getDimensionPixelSize(R.dimen.padding_2xl) to resources.getDimensionPixelSize(R.dimen.padding_2xl)
                }
            }
            else -> resources.getDimensionPixelSize(R.dimen.padding_lg) to resources.getDimensionPixelSize(R.dimen.padding_lg)
        }
    }

    private fun setIconMargins(size: Int, hasLeftIcon: Boolean, hasRightIcon: Boolean) {
        val margin = when (size) {
            0 -> resources.getDimensionPixelSize(R.dimen.spacing_xs) // 4dp
            else -> resources.getDimensionPixelSize(R.dimen.spacing_sm) // 8dp
        }

        // 왼쪽 아이콘 마진
        val leftIconParams = leftIcon.layoutParams as? ViewGroup.MarginLayoutParams
        leftIconParams?.marginEnd = if (hasLeftIcon) margin else 0
        leftIcon.layoutParams = leftIconParams

        // 오른쪽 아이콘 마진
        val rightIconParams = rightIcon.layoutParams as? ViewGroup.MarginLayoutParams
        rightIconParams?.marginStart = if (hasRightIcon) margin else 0
        rightIcon.layoutParams = rightIconParams
    }

    private fun updateButtonState(hierarchy: Int, type: Int, state: Int) {
        when (hierarchy) {
            0 -> when (type) { // primary
                0 -> when (state) { // filled
                    0 -> applyButtonStyles(R.drawable.bg_action_button_primary_filled_default, R.color.fg_onprimary, true)
                    1 -> applyButtonStyles(R.drawable.bg_action_button_primary_filled_pressed, R.color.fg_onprimary, true)
                    2 -> applyButtonStyles(R.drawable.bg_action_button_disabled, R.color.fg_disabled, false)
                }
            }
            1 -> when (type) { // secondary
                0 -> when (state) { // filled
                    0 -> applyButtonStyles(R.drawable.bg_action_button_secondary_filled_default, R.color.fg_neutral_secondary, true)
                    1 -> applyButtonStyles(R.drawable.bg_action_button_secondary_filled_pressed, R.color.fg_neutral_secondary, true)
                    2 -> applyButtonStyles(R.drawable.bg_action_button_disabled, R.color.fg_disabled, false)
                }
                1 -> when (state) { // subtle
                    0 -> applyButtonStyles(R.drawable.bg_action_button_secondary_subtitle_default, R.color.fg_brand_default, true)
                    1 -> applyButtonStyles(R.drawable.bg_action_button_secondary_subtitle_pressed, R.color.fg_brand_pressed, true)
                    2 -> applyButtonStyles(R.drawable.bg_action_button_disabled, R.color.fg_disabled, false)
                }
            }
            2 -> when (type) { // tertiary
                2 -> when (state) { // ghost
                    0 -> applyButtonStyles(R.drawable.bg_action_button_tertiary_ghost_default, R.color.fg_neutral_tertiary, true)
                    1 -> applyButtonStyles(R.drawable.bg_action_button_tertiary_ghost_pressed, R.color.fg_neutral_tertiary, true)
                    2 -> applyButtonStyles(R.drawable.bg_action_button_tertiary_ghost_disabled, R.color.fg_disabled, false)
                }
                3 -> when (state) { // outline
                    0 -> applyButtonStyles(R.drawable.bg_action_button_tertiary_outline_default, R.color.fg_neutral_tertiary, true)
                    1 -> applyButtonStyles(R.drawable.bg_action_button_tertiary_outline_pressed, R.color.fg_neutral_tertiary, true)
                    2 -> applyButtonStyles(R.drawable.bg_action_button_tertiary_outline_disabled, R.color.fg_disabled, false)
                }
            }
        }
    }

    private fun applyButtonStyles(backgroundResId: Int, textColorResId: Int, isEnabled: Boolean) {
        // 배경 설정
        background = context.getDrawable(backgroundResId)

        // 텍스트 및 아이콘 색상 설정
        val color = context.getColor(textColorResId)
        buttonText.setTextColor(color)
        leftIcon.setColorFilter(color)
        rightIcon.setColorFilter(color)

        // 버튼 활성화 상태 설정
        this.isEnabled = isEnabled
    }
}