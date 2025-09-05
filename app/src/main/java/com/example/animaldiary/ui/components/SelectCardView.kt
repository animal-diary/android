package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.animaldiary.R

class SelectCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    enum class CardAlign(val value: Int) {
        CENTER(0), LEFT(1);
        companion object {
            fun from(value: Int): CardAlign = values().first { it.value == value }
        }
    }

    private var cardAlign: CardAlign = CardAlign.CENTER
    private var showIllust: Boolean = true
    private var cardTitle: String = ""
    private var cardDescription: String = ""
    private var isCardSelected = false

    private val allLayouts = listOf(
        R.id.center_layout_illust,
        R.id.center_layout_no_illust,
        R.id.left_layout_illust,
        R.id.left_layout_no_illust
    )


    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.component_select_card, this, true)

        attrs?.let {
            val sc = context.obtainStyledAttributes(it, R.styleable.SelectCardView, 0, 0)
            cardAlign = CardAlign.from(sc.getInt(R.styleable.SelectCardView_sc_align, 0))
            showIllust = sc.getBoolean(R.styleable.SelectCardView_sc_showIllust, true)
            cardTitle = sc.getString(R.styleable.SelectCardView_sc_title) ?: ""
            cardDescription = sc.getString(R.styleable.SelectCardView_sc_description) ?: ""
            sc.recycle()
        }

        applyLayout()
        updateSelectedState()
    }

    private fun applyLayout() {
        val allLayouts = listOf(
            R.id.center_layout_illust,
            R.id.center_layout_no_illust,
            R.id.left_layout_illust,
            R.id.left_layout_no_illust
        )

        allLayouts.forEach { findViewById<View>(it)?.visibility = View.GONE }

        val targetLayoutId = when {
            cardAlign == CardAlign.CENTER && showIllust -> R.id.center_layout_illust
            cardAlign == CardAlign.CENTER && !showIllust -> R.id.center_layout_no_illust
            cardAlign == CardAlign.LEFT && showIllust -> R.id.left_layout_illust
            else -> R.id.left_layout_no_illust
        }

        findViewById<View>(targetLayoutId)?.visibility = View.VISIBLE

        // 텍스트 설정
        when (targetLayoutId) {
            R.id.center_layout_illust -> {
                findViewById<TextView>(R.id.card_title_center)?.text = cardTitle
                findViewById<TextView>(R.id.card_description_center)?.text = cardDescription
            }
            R.id.center_layout_no_illust -> {
                findViewById<TextView>(R.id.title_center_no_illust)?.text = cardTitle
                findViewById<TextView>(R.id.description_center_no_illust)?.text = cardDescription
            }
            R.id.left_layout_illust -> {
                findViewById<TextView>(R.id.card_title_left)?.text = cardTitle
                findViewById<TextView>(R.id.card_description_left)?.text = cardDescription
            }
            R.id.left_layout_no_illust -> {
                findViewById<TextView>(R.id.title_left_no_illust)?.text = cardTitle
                findViewById<TextView>(R.id.description_left_no_illust)?.text = cardDescription
            }
        }
    }

    fun setScTitle(title: String) {
        this.cardTitle = title
        applyLayout()
        updateSelectedState()
    }

    fun setScDescription(description: String) {
        this.cardDescription = description
        applyLayout()
        updateSelectedState()
    }

    fun setAlign(align: CardAlign) {
        cardAlign = align
        applyLayout()
    }

    fun setShowIllust(show: Boolean) {
        showIllust = show
        applyLayout()
    }

    fun isSelectedCard(): Boolean {
        return isCardSelected
    }

    fun setSelectedCard(selected: Boolean) {
        isCardSelected = selected
        updateSelectedState()
    }

    private fun updateSelectedState() {
        // 선택 상태에 따라 글씨 색상을 결정
        val titleColor = if (isCardSelected) context.getColor(R.color.fg_selected) else context.getColor(R.color.fg_neutral_secondary)
        val descriptionColor = if (isCardSelected) context.getColor(R.color.fg_selected) else context.getColor(R.color.fg_neutral_tertiary)

        // 현재 활성화된 레이아웃의 ID를 찾음
        val targetLayoutId = when {
            cardAlign == CardAlign.CENTER && showIllust -> R.id.center_layout_illust
            cardAlign == CardAlign.CENTER && !showIllust -> R.id.center_layout_no_illust
            cardAlign == CardAlign.LEFT && showIllust -> R.id.left_layout_illust
            else -> R.id.left_layout_no_illust
        }

        // 활성화된 레이아웃에 따라 텍스트 색상을 변경
        when (targetLayoutId) {
            R.id.center_layout_illust -> {
                findViewById<TextView>(R.id.card_title_center)?.setTextColor(titleColor)
                findViewById<TextView>(R.id.card_description_center)?.setTextColor(descriptionColor)
            }
            R.id.center_layout_no_illust -> {
                findViewById<TextView>(R.id.title_center_no_illust)?.setTextColor(titleColor)
                findViewById<TextView>(R.id.description_center_no_illust)?.setTextColor(descriptionColor)
            }
            R.id.left_layout_illust -> {
                findViewById<TextView>(R.id.card_title_left)?.setTextColor(titleColor)
                findViewById<TextView>(R.id.card_description_left)?.setTextColor(descriptionColor)
            }
            R.id.left_layout_no_illust -> {
                findViewById<TextView>(R.id.title_left_no_illust)?.setTextColor(titleColor)
                findViewById<TextView>(R.id.description_left_no_illust)?.setTextColor(descriptionColor)
            }
        }

        // 현재 활성화된 (targetLayoutId) 레이아웃의 배경을 선택 상태에 따라 변경
        val selectedLayoutView = findViewById<View>(targetLayoutId)
        selectedLayoutView?.setBackgroundResource(
            if (isCardSelected) R.drawable.bg_select_card_selected
            else R.drawable.bg_select_card_default
        )
    }
}
