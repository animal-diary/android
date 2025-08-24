package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.animaldiary.R

class BottomSheetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val rootContainer: ViewGroup
    private val handle: View
    private val handleContainer: ViewGroup
    private val navigationContainer: ConstraintLayout
    private val leftIcon: ImageView
    private val rightIcon1: ImageView
    private val rightIcon2: ImageView
    private val titleText: TextView
    private val contentContainer: FrameLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.component_bottom_sheet, this, true)

        rootContainer = findViewById(R.id.bottom_sheet_root)
        handle = findViewById(R.id.bottom_sheet_handle)
        handleContainer = findViewById(R.id.bottom_sheet_handle_container)
        navigationContainer = findViewById(R.id.bottom_sheet_navigation)
        leftIcon = findViewById(R.id.nav_left_icon)
        rightIcon1 = findViewById(R.id.nav_right_icon_1)
        rightIcon2 = findViewById(R.id.nav_right_icon_2)
        titleText = findViewById(R.id.nav_title)
        contentContainer = findViewById(R.id.bottom_sheet_content)

        // 기본적으로 모든 아이콘을 숨김
        leftIcon.visibility = View.GONE
        rightIcon1.visibility = View.GONE
        rightIcon2.visibility = View.GONE

        // attrs.xml 속성 처리
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.BottomSheetView)
            try {
                // 제목 설정
                val title = ta.getString(R.styleable.BottomSheetView_bs_title)
                val showTitle = ta.getBoolean(R.styleable.BottomSheetView_bs_showTitle, true)
                val showNavigation = ta.getBoolean(R.styleable.BottomSheetView_bs_showNavigation, true)
                val showHandle = ta.getBoolean(R.styleable.BottomSheetView_bs_showHandle, true)

                if (title != null && showTitle) {
                    setTitle(title)
                } else {
                    setTitleVisible(false)
                }

                setNavigationVisible(showNavigation)
                setHandleVisible(showHandle)

                val leftIconRes = ta.getResourceId(R.styleable.BottomSheetView_bs_leftIcon, -1)
                if (leftIconRes != -1) {
                    setLeftIcon(leftIconRes)
                }

                val rightIcon1Res = ta.getResourceId(R.styleable.BottomSheetView_bs_rightIcon1, -1)
                if (rightIcon1Res != -1) {
                    setRightIcon1(rightIcon1Res)
                }

                val rightIcon2Res = ta.getResourceId(R.styleable.BottomSheetView_bs_rightIcon2, -1)
                if (rightIcon2Res != -1) {
                    setRightIcon2(rightIcon2Res)
                }
            } finally {
                ta.recycle()
            }
        }
    }

    val handleView: View
        get() = handle

    val rootContainerView: ViewGroup
        get() = rootContainer

    val handleContainerView: ViewGroup
        get() = handleContainer

    // Handle 관련 메소드
    fun setHandleVisible(visible: Boolean) {
        handle.visibility = if (visible) View.VISIBLE else View.GONE
    }

    // Navigation 관련 메소드
    fun setNavigationVisible(visible: Boolean) {
        navigationContainer.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setTitle(text: String) {
        titleText.text = text
        titleText.visibility = View.VISIBLE
    }

    fun setTitleVisible(visible: Boolean) {
        titleText.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setLeftIcon(resId: Int, onClick: (() -> Unit)? = null) {
        leftIcon.setImageResource(resId)
        leftIcon.visibility = View.VISIBLE
        onClick?.let { leftIcon.setOnClickListener { it() } }
    }

    fun setRightIcon1(resId: Int, onClick: (() -> Unit)? = null) {
        rightIcon1.setImageResource(resId)
        rightIcon1.visibility = View.VISIBLE
        onClick?.let { rightIcon1.setOnClickListener { it() } }
    }

    fun setRightIcon2(resId: Int, onClick: (() -> Unit)? = null) {
        rightIcon2.setImageResource(resId)
        rightIcon2.visibility = View.VISIBLE
        onClick?.let { rightIcon2.setOnClickListener { it() } }
    }

    fun setLeftIconVisible(visible: Boolean) {
        leftIcon.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setRightIcon1Visible(visible: Boolean) {
        rightIcon1.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setRightIcon2Visible(visible: Boolean) {
        rightIcon2.visibility = if (visible) View.VISIBLE else View.GONE
    }

    // Content 관련 메소드
    fun setContent(view: View) {
        contentContainer.removeAllViews()
        contentContainer.addView(view)
    }

    fun getContentContainer(): FrameLayout {
        return contentContainer
    }

    // 클릭 리스너 설정
    fun setLeftIconClickListener(onClick: () -> Unit) {
        leftIcon.setOnClickListener { onClick() }
    }

    fun setRightIcon1ClickListener(onClick: () -> Unit) {
        rightIcon1.setOnClickListener { onClick() }
    }

    fun setRightIcon2ClickListener(onClick: () -> Unit) {
        rightIcon2.setOnClickListener { onClick() }
    }

    fun Int.dpToPx(): Int {
        return (this * this@BottomSheetView.context.resources.displayMetrics.density + 0.5f).toInt()
    }

    fun Float.dpToPx(): Float {
        return this * this@BottomSheetView.context.resources.displayMetrics.density
    }
}