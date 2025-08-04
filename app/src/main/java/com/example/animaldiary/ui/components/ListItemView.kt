package com.example.animaldiary.ui.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.animaldiary.R

class ListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    // 뷰들 (listBackground 제거)
    private lateinit var listContainer: ConstraintLayout
    private lateinit var illustImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var contentContainer: LinearLayout
    private lateinit var iconImageView: ImageView

    // 속성
    private var showBackground: Boolean = true
    private var showIllust: Boolean = true
    private var showDescription: Boolean = true
    private var showIcon: Boolean = true
    private var itemState: ListState = ListState.NORMAL

    // 콘텐츠
    private var titleText: String = "Title"
    private var descriptionText: String = "Fill in the Description"

    // 클릭 리스너
    private var onItemClickListener: (() -> Unit)? = null

    init {
        // 레이아웃 인플레이트
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.list_item_layout, this, true)

        // 뷰 찾기 (listBackground 제거)
        listContainer = findViewById(R.id.list_container)
        illustImageView = findViewById(R.id.iv_illust)
        titleTextView = findViewById(R.id.tv_title)
        descriptionTextView = findViewById(R.id.tv_description)
        contentContainer = findViewById(R.id.ll_content_container)
        iconImageView = findViewById(R.id.iv_icon)

        // XML 속성 읽기
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ListItemView, 0, 0)
            try {
                showBackground = typedArray.getBoolean(R.styleable.ListItemView_showBackground, true)
                showIllust = typedArray.getBoolean(R.styleable.ListItemView_showIllust, true)
                showDescription = typedArray.getBoolean(R.styleable.ListItemView_showDescription, true)
                showIcon = typedArray.getBoolean(R.styleable.ListItemView_showIcon, true)

                titleText = typedArray.getString(R.styleable.ListItemView_listTitle) ?: "Title"
                descriptionText = typedArray.getString(R.styleable.ListItemView_listDescription) ?: "Fill in the Description"

                val illustRes = typedArray.getResourceId(R.styleable.ListItemView_illustDrawable, -1)
                if (illustRes != -1) {
                    illustImageView.setImageResource(illustRes)
                }

                val stateValue = typedArray.getInt(R.styleable.ListItemView_listState, 0)
                itemState = ListState.values()[stateValue]

            } finally {
                typedArray.recycle()
            }
        }

        // 초기 상태 설정
        setupView()
        updateVisibility()
        updateState()

        // 클릭 리스너 설정
        setOnClickListener {
            onItemClickListener?.invoke()
        }
    }

    private fun setupView() {
        // 콘텐츠 설정
        titleTextView.text = titleText
        descriptionTextView.text = descriptionText
    }

    private fun updateVisibility() {
        // list_background View는 제거했으므로 이 라인 삭제
        illustImageView.visibility = if (showIllust) View.VISIBLE else View.GONE
        descriptionTextView.visibility = if (showDescription) View.VISIBLE else View.GONE
        iconImageView.visibility = if (showIcon) View.VISIBLE else View.GONE

        // 배경 적용 로직을 여기서 처리
        updateBackground()
    }

    private fun updateBackground() {
        if (showBackground) {
            when (itemState) {
                ListState.NORMAL -> {
                    listContainer.setBackgroundResource(R.drawable.list_background_default)
                }
                ListState.PRESSED -> {
                    listContainer.setBackgroundResource(R.drawable.list_background_pressed)
                }
            }
        } else {
            listContainer.setBackgroundResource(android.R.color.transparent)
        }
    }

    private fun updateState() {
        when (itemState) {
            ListState.NORMAL -> {
                alpha = 1f
                titleTextView.setTextColor(ContextCompat.getColor(context, R.color.fg_neutral_primary))
            }
            ListState.PRESSED -> {
                alpha = 0.8f
                titleTextView.setTextColor(ContextCompat.getColor(context, R.color.fg_neutral_primary))
            }
        }
        // 상태 변경 시에도 배경 업데이트
        updateBackground()
    }

    // 공개 메서드들
    fun setTitle(title: String) {
        this.titleText = title
        titleTextView.text = title
    }

    fun setDescription(description: String) {
        this.descriptionText = description
        descriptionTextView.text = description
    }

    fun setIllustResource(resId: Int) {
        illustImageView.setImageResource(resId)
    }

    fun setIllustDrawable(drawable: Drawable?) {
        illustImageView.setImageDrawable(drawable)
    }

    fun setIconResource(resId: Int) {
        iconImageView.setImageResource(resId)
    }

    fun setIconDrawable(drawable: Drawable?) {
        iconImageView.setImageDrawable(drawable)
    }

    fun setState(state: ListState) {
        this.itemState = state
        updateState()
    }

    fun setShowBackground(show: Boolean) {
        showBackground = show
        updateVisibility()
    }

    fun setShowIllust(show: Boolean) {
        showIllust = show
        updateVisibility()
    }

    fun setShowDescription(show: Boolean) {
        showDescription = show
        updateVisibility()
    }

    fun setShowIcon(show: Boolean) {
        showIcon = show
        updateVisibility()
    }

    fun setOnItemClickListener(listener: () -> Unit) {
        onItemClickListener = listener
    }

    private fun dpToPx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}

enum class ListState {
    NORMAL, PRESSED
}