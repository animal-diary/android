package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.animaldiary.R

class ButtonContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val horizontalContainer: LinearLayout
    private val verticalContainer: LinearLayout

    private lateinit var primaryButtonHorizontal: ActionButtonView
    private lateinit var secondaryButtonHorizontal: ActionButtonView
    private lateinit var primaryButtonVertical: ActionButtonView
    private lateinit var secondaryButtonVertical: ActionButtonView

    init {
        // 레이아웃 인플레이트
        LayoutInflater.from(context).inflate(R.layout.component_button_container, this, true)

        horizontalContainer = findViewById(R.id.horizontalContainer)
        verticalContainer = findViewById(R.id.verticalContainer)

        // 버튼 참조 초기화
        primaryButtonHorizontal = findViewById(R.id.primaryActionButtonHorizontal)
        secondaryButtonHorizontal = findViewById(R.id.secondaryActionButtonHorizontal)
        primaryButtonVertical = findViewById(R.id.primaryActionButtonVertical)
        secondaryButtonVertical = findViewById(R.id.secondaryActionButtonVertical)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ButtonContainerView, 0, 0)

            try {
                val alignment = typedArray.getInt(R.styleable.ButtonContainerView_bc_alignment, 0)
                val showAdditionalButton = typedArray.getBoolean(R.styleable.ButtonContainerView_bc_showAdditionalButton, true)

                // Primary 버튼 속성 읽기
                val primaryText = typedArray.getString(R.styleable.ButtonContainerView_bc_primaryText) ?: "Action"
                val primarySize = typedArray.getInt(R.styleable.ButtonContainerView_bc_primarySize, 2) // 기본값 lg
                val primaryHierarchy = typedArray.getInt(R.styleable.ButtonContainerView_bc_primaryHierarchy, 0) // 기본값 primary
                val primaryType = typedArray.getInt(R.styleable.ButtonContainerView_bc_primaryType, 0) // 기본값 filled
                val primaryState = typedArray.getInt(R.styleable.ButtonContainerView_bc_primaryState, 0) // 기본값 normal
                val primaryLeftIcon = typedArray.getResourceId(R.styleable.ButtonContainerView_bc_primaryLeftIcon, 0)
                val primaryRightIcon = typedArray.getResourceId(R.styleable.ButtonContainerView_bc_primaryRightIcon, 0)
                val primaryLeftIconVisible = typedArray.getBoolean(R.styleable.ButtonContainerView_bc_primaryLeftIconVisible, false)
                val primaryRightIconVisible = typedArray.getBoolean(R.styleable.ButtonContainerView_bc_primaryRightIconVisible, false)

                // Secondary 버튼 속성 읽기
                val secondaryText = typedArray.getString(R.styleable.ButtonContainerView_bc_secondaryText) ?: "Action"
                val secondarySize = typedArray.getInt(R.styleable.ButtonContainerView_bc_secondarySize, 2) // 기본값 lg
                val secondaryHierarchy = typedArray.getInt(R.styleable.ButtonContainerView_bc_secondaryHierarchy, 1) // 기본값 secondary
                val secondaryType = typedArray.getInt(R.styleable.ButtonContainerView_bc_secondaryType, 1) // 기본값 subtle
                val secondaryState = typedArray.getInt(R.styleable.ButtonContainerView_bc_secondaryState, 0) // 기본값 normal
                val secondaryLeftIcon = typedArray.getResourceId(R.styleable.ButtonContainerView_bc_secondaryLeftIcon, 0)
                val secondaryRightIcon = typedArray.getResourceId(R.styleable.ButtonContainerView_bc_secondaryRightIcon, 0)
                val secondaryLeftIconVisible = typedArray.getBoolean(R.styleable.ButtonContainerView_bc_secondaryLeftIconVisible, false)
                val secondaryRightIconVisible = typedArray.getBoolean(R.styleable.ButtonContainerView_bc_secondaryRightIconVisible, false)

                // 컨테이너 업데이트
                updateContainer(alignment, showAdditionalButton)

                // 버튼 스타일 적용
                applyButtonStyles(
                    primaryText, primarySize, primaryHierarchy, primaryType, primaryState,
                    primaryLeftIcon, primaryRightIcon, primaryLeftIconVisible, primaryRightIconVisible,
                    secondaryText, secondarySize, secondaryHierarchy, secondaryType, secondaryState,
                    secondaryLeftIcon, secondaryRightIcon, secondaryLeftIconVisible, secondaryRightIconVisible
                )

            } finally {
                typedArray.recycle()
            }
        }
    }

    private fun updateContainer(alignment: Int, showAdditionalButton: Boolean) {
        val isHorizontal = alignment == 0

        // 정렬에 따라 적절한 컨테이너만 보이도록 설정
        horizontalContainer.isVisible = isHorizontal
        verticalContainer.isVisible = !isHorizontal

        // 보조 버튼 가시성 설정
        secondaryButtonHorizontal.isVisible = showAdditionalButton && isHorizontal
        secondaryButtonVertical.isVisible = showAdditionalButton && !isHorizontal
    }

    private fun applyButtonStyles(
        primaryText: String, primarySize: Int, primaryHierarchy: Int, primaryType: Int, primaryState: Int,
        primaryLeftIcon: Int, primaryRightIcon: Int, primaryLeftIconVisible: Boolean, primaryRightIconVisible: Boolean,
        secondaryText: String, secondarySize: Int, secondaryHierarchy: Int, secondaryType: Int, secondaryState: Int,
        secondaryLeftIcon: Int, secondaryRightIcon: Int, secondaryLeftIconVisible: Boolean, secondaryRightIconVisible: Boolean
    ) {
        // Primary 버튼들에 스타일 적용
        applyButtonStyle(
            primaryButtonHorizontal, primaryText, primarySize, primaryHierarchy, primaryType, primaryState,
            primaryLeftIcon, primaryRightIcon, primaryLeftIconVisible, primaryRightIconVisible
        )
        applyButtonStyle(
            primaryButtonVertical, primaryText, primarySize, primaryHierarchy, primaryType, primaryState,
            primaryLeftIcon, primaryRightIcon, primaryLeftIconVisible, primaryRightIconVisible
        )

        // Secondary 버튼들에 스타일 적용
        applyButtonStyle(
            secondaryButtonHorizontal, secondaryText, secondarySize, secondaryHierarchy, secondaryType, secondaryState,
            secondaryLeftIcon, secondaryRightIcon, secondaryLeftIconVisible, secondaryRightIconVisible
        )
        applyButtonStyle(
            secondaryButtonVertical, secondaryText, secondarySize, secondaryHierarchy, secondaryType, secondaryState,
            secondaryLeftIcon, secondaryRightIcon, secondaryLeftIconVisible, secondaryRightIconVisible
        )
    }

    private fun applyButtonStyle(
        button: ActionButtonView,
        text: String,
        size: Int,
        hierarchy: Int,
        type: Int,
        state: Int,
        leftIcon: Int,
        rightIcon: Int,
        leftIconVisible: Boolean,
        rightIconVisible: Boolean
    ) {
        button.apply {
            // ActionButtonView의 메서드를 직접 호출할 수 없으므로,
            // 프로그래매틱하게 속성을 설정하는 메서드를 추가해야 합니다.
            updateButtonProgrammatically(
                text, size, hierarchy, type, state,
                leftIcon, rightIcon, leftIconVisible, rightIconVisible
            )
        }
    }
}