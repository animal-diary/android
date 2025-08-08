package com.example.animaldiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.animaldiary.R

class ModalView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val titleContainer: View
    private val tvTitle: TextView
    private val tvDescription: TextView
    private val tvContent: TextView
    private val contentContainer: LinearLayout
    private val leftButton: Button
    private val rightButton: Button

    private var align: Int = 0 // 0: center, 1: left

    init {
        LayoutInflater.from(context).inflate(R.layout.component_modal, this, true)
        orientation = VERTICAL

        // 뷰 참조
        titleContainer = findViewById(R.id.modal_titleContainer)
        tvTitle = findViewById(R.id.modal_title)
        tvDescription = findViewById(R.id.modal_description)
        tvContent = findViewById(R.id.modal_content)
        contentContainer = findViewById(R.id.modal_content_container)
        leftButton = findViewById(R.id.modal_btn_left)
        rightButton = findViewById(R.id.modal_btn_right)

        // XML 속성 처리
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.ModalView, 0, 0)
            try {
                // 제목 및 설명
                setTitle(a.getString(R.styleable.ModalView_modal_Title))
                setDescription(a.getString(R.styleable.ModalView_modal_Description))
                showTitle(a.getBoolean(R.styleable.ModalView_showTitle, true))
                showDescription(a.getBoolean(R.styleable.ModalView_showDescription, true))
                showContent(a.getBoolean(R.styleable.ModalView_showContent, true))
                setContentText(a.getString(R.styleable.ModalView_modal_content))

                // 정렬 속성 처리
                align = a.getInt(R.styleable.ModalView_modal_align, 0)
                updateAlignment()

                // 버튼 텍스트 및 가시성
                setLeftButtonText(a.getString(R.styleable.ModalView_leftButtonText))
                setRightButtonText(a.getString(R.styleable.ModalView_rightButtonText))
                showLeftButton(a.getBoolean(R.styleable.ModalView_showLeftButton, true))
                showRightButton(a.getBoolean(R.styleable.ModalView_showRightButton, true))

            } finally {
                a.recycle()
            }
        }
    }

    // public 메서드
    fun setTitle(title: String?) {
        tvTitle.text = title
        tvTitle.visibility = if (title.isNullOrBlank()) View.GONE else View.VISIBLE
    }

    fun setDescription(description: String?) {
        tvDescription.text = description
        tvDescription.visibility = if (description.isNullOrBlank()) View.GONE else View.VISIBLE
    }

    fun setContentText(content: String?) {
        tvContent.text = content
        tvContent.visibility = if (content.isNullOrBlank()) View.GONE else View.VISIBLE
        updateAlignment() // 텍스트 변경 후 정렬 재적용
    }

    fun setAlignment(alignment: Int) {
        this.align = alignment
        updateAlignment()
    }

    private fun updateAlignment() {
        val gravity = if (align == 1) Gravity.START else Gravity.CENTER_HORIZONTAL
        tvTitle.gravity = gravity
        tvDescription.gravity = gravity
        tvContent.gravity = gravity
    }

    fun showTitle(show: Boolean) {
        tvTitle.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showDescription(show: Boolean) {
        tvDescription.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showContent(show: Boolean) {
        contentContainer.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setLeftButtonText(text: String?) {
        leftButton.text = text
    }

    fun setRightButtonText(text: String?) {
        rightButton.text = text
    }

    fun showLeftButton(show: Boolean) {
        leftButton.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showRightButton(show: Boolean) {
        rightButton.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun getContentContainer(): LinearLayout {
        return contentContainer
    }

    fun setOnLeftButtonClickListener(listener: (View) -> Unit) {
        leftButton.setOnClickListener(listener)
    }

    fun setOnRightButtonClickListener(listener: (View) -> Unit) {
        rightButton.setOnClickListener(listener)
    }
}