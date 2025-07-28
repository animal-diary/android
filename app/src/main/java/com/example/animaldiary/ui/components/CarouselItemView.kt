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

class CarouselItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    // 뷰
    private var containerView: ConstraintLayout
    private var illustImageView: ImageView
    private var titleTextView: TextView
    private var subTextView: TextView
    private var descriptionTextView: TextView
    private var iconImageView: ImageView
    private var titleContainer: LinearLayout

    // 속성
    private var showDescription: Boolean = true
    private var showIllust: Boolean = true
    private var showIcon: Boolean = true
    private var showSub: Boolean = true
    private var itemState: CarouselState = CarouselState.NORMAL

    // 콘텐츠
    private var titleText: String = "Fill the Title"
    private var subText: String = "Sub"
    private var descriptionText: String = "Description"

    // 클릭 리스너
    private var onCardClickListener: (() -> Unit)? = null

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.carousel_item_layout, this, true)

        // 뷰 찾기
        containerView = findViewById(R.id.carousel_container)
        illustImageView = findViewById(R.id.iv_illust)
        titleTextView = findViewById(R.id.tv_title)
        subTextView = findViewById(R.id.tv_sub)
        descriptionTextView = findViewById(R.id.tv_description)
        iconImageView = findViewById(R.id.iv_icon)
        titleContainer = findViewById(R.id.ll_title_container)

        // XML 속성 읽기
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CarouselItemView, 0, 0)
            try {
                showDescription = typedArray.getBoolean(R.styleable.CarouselItemView_showDescription, true)
                showIllust = typedArray.getBoolean(R.styleable.CarouselItemView_showIllust, true)
                showIcon = typedArray.getBoolean(R.styleable.CarouselItemView_showIcon, true)
                showSub = typedArray.getBoolean(R.styleable.CarouselItemView_showSub, true)

                titleText = typedArray.getString(R.styleable.CarouselItemView_carouselTitle) ?: "Fill the Title"
                subText = typedArray.getString(R.styleable.CarouselItemView_carouselSubTitle) ?: "Sub"
                descriptionText = typedArray.getString(R.styleable.CarouselItemView_carouselDescription) ?: "Description"

                val illustRes = typedArray.getResourceId(R.styleable.CarouselItemView_illustDrawable, -1)
                if (illustRes != -1) {
                    illustImageView.setImageResource(illustRes)
                }

                val iconRes = typedArray.getResourceId(R.styleable.CarouselItemView_iconDrawable, R.drawable.ic_chevron_right)
                iconImageView.setImageResource(iconRes)

                val stateValue = typedArray.getInt(R.styleable.CarouselItemView_carouselState, 0)
                itemState = CarouselState.values()[stateValue]

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
            onCardClickListener?.invoke()
        }
    }

    private fun setupView() {
        // 콘텐츠 설정
        titleTextView.text = titleText
        subTextView.text = subText
        descriptionTextView.text = descriptionText

        // 기본 아이콘이 설정되지 않은 경우 설정
        if (iconImageView.drawable == null) {
            iconImageView.setImageResource(R.drawable.ic_chevron_right)
        }
    }

    private fun updateVisibility() {
        illustImageView.visibility = if (showIllust) View.VISIBLE else View.GONE
        subTextView.visibility = if (showSub) View.VISIBLE else View.GONE
        descriptionTextView.visibility = if (showDescription) View.VISIBLE else View.GONE
        iconImageView.visibility = if (showIcon) View.VISIBLE else View.GONE
    }

    private fun updateState() {
        when (itemState) {
            CarouselState.NORMAL -> {
                containerView.setBackgroundResource(R.drawable.carousel_background_default)
                alpha = 1f
                titleTextView.setTextColor(ContextCompat.getColor(context, R.color.fg_neutral_primary))
            }
            CarouselState.FOCUSED -> {
                containerView.setBackgroundResource(R.drawable.carousel_background_focused)
                alpha = 1f
                titleTextView.setTextColor(ContextCompat.getColor(context, R.color.fg_neutral_primary))
            }
            CarouselState.PRESSED -> {
                containerView.setBackgroundResource(R.drawable.carousel_background_pressed)
                alpha = 0.8f
                titleTextView.setTextColor(ContextCompat.getColor(context, R.color.fg_neutral_primary))
            }
            CarouselState.EMPTY -> {
                containerView.setBackgroundResource(R.drawable.carousel_background_empty)
                alpha = 0.6f
                titleTextView.setTextColor(ContextCompat.getColor(context, R.color.fg_neutral_tertiary))
            }
        }
    }

    // 공개 메서드들
    fun setTitle(title: String) {
        this.titleText = title
        titleTextView.text = title
    }

    fun setSubTitle(subTitle: String) {
        this.subText = subTitle
        subTextView.text = subTitle
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

    fun setState(state: CarouselState) {
        this.itemState = state
        updateState()
    }

    fun setShowDescription(show: Boolean) {
        showDescription = show
        updateVisibility()
    }

    fun setShowIllust(show: Boolean) {
        showIllust = show
        updateVisibility()
    }

    fun setShowIcon(show: Boolean) {
        showIcon = show
        updateVisibility()
    }

    fun setShowSub(show: Boolean) {
        showSub = show
        updateVisibility()
    }

    fun setOnCardClickListener(listener: () -> Unit) {
        onCardClickListener = listener
    }

    private fun dpToPx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}

enum class CarouselState {
    NORMAL, FOCUSED, PRESSED, EMPTY
}