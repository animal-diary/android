package com.example.animaldiary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.example.animaldiary.databinding.ActivityTestTextButtonBinding
import com.example.animaldiary.ui.components.TextButtonView

class TestTextButtonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestTextButtonBinding

    // 색상 정의 - colors.xml의 시스템 토큰 사용
    private val defaultTextColor by lazy { ContextCompat.getColor(this, R.color.fg_brand_default) }
    private val pressedTextColor by lazy { ContextCompat.getColor(this, R.color.fg_brand_pressed) }
    private val disabledTextColor by lazy { ContextCompat.getColor(this, R.color.fg_disabled) }
    private val defaultIconColor by lazy { ContextCompat.getColor(this, R.color.fg_brand_default) }
    private val pressedIconColor by lazy { ContextCompat.getColor(this, R.color.fg_brand_pressed) }
    private val disabledIconColor by lazy { ContextCompat.getColor(this, R.color.fg_disabled) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestTextButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTextButton()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTextButton() {
        val button = binding.testTextButton

        button.setText("더보기")
        button.setLeftIcon(R.drawable.ic_chevron_left)
        button.setRightIcon(null)

        // 초기 상태
        setButtonNormalState(button)

        // 누를 때 색상 변경
        button.setOnTouchListener { _, event ->
            if (!button.isEnabled) return@setOnTouchListener false

            when (event.action) {
                MotionEvent.ACTION_DOWN -> setButtonPressedState(button)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> setButtonNormalState(button)
            }

            false  // 꼭 false로 해야 onClick 이벤트가 발생함
        }

        // 클릭 처리 (토스트)
        button.setOnClickListener {
            performButtonClick()
        }

//        // 테스트용 자동 비활성화
//        setupDisableButton(button)
    }

    private fun setButtonNormalState(button: TextButtonView) {
        // 비활성화 상태 확인
        if (!button.isEnabled) {
            setButtonDisabledState(button)
            return
        }

        // 텍스트 색상 변경
        button.getTextView().setTextColor(defaultTextColor)

        // 아이콘 색상 변경 (아이콘이 있는 경우)
        updateIconColors(button, defaultIconColor)
    }

    private fun setButtonPressedState(button: TextButtonView) {
        // 비활성화 상태에서는 pressed 상태 무시
        if (!button.isEnabled) {
            return
        }

        // 텍스트 색상 변경
        button.getTextView().setTextColor(pressedTextColor)

        // 아이콘 색상 변경 (아이콘이 있는 경우)
        updateIconColors(button, pressedIconColor)
    }

    private fun setButtonDisabledState(button: TextButtonView) {
        // 텍스트 색상 변경
        button.getTextView().setTextColor(disabledTextColor)

        // 아이콘 색상 변경 (아이콘이 있는 경우)
        updateIconColors(button, disabledIconColor)
    }

    private fun updateIconColors(button: TextButtonView, color: Int) {
        val colorStateList = android.content.res.ColorStateList.valueOf(color)

        if (button.getLeftIconView().visibility == android.view.View.VISIBLE) {
            ImageViewCompat.setImageTintList(button.getLeftIconView(), colorStateList)
        }
        if (button.getRightIconView().visibility == android.view.View.VISIBLE) {
            ImageViewCompat.setImageTintList(button.getRightIconView(), colorStateList)
        }
    }

    // 테스트를 위한 비활성화 토글 버튼
//    private fun setupDisableButton(textButton: com.example.animaldiary.ui.components.TextButtonView) {
//        // 3초 후 비활성화, 6초 후 다시 활성화 (테스트용)
//        textButton.postDelayed({
//            textButton.setButtonEnabled(false)
//            setButtonDisabledState(textButton)
//            Toast.makeText(this, "버튼 비활성화됨", Toast.LENGTH_SHORT).show()
//
//            textButton.postDelayed({
//                textButton.setButtonEnabled(true)
//                setButtonNormalState(textButton)
//                Toast.makeText(this, "버튼 활성화됨", Toast.LENGTH_SHORT).show()
//            }, 3000)
//        }, 3000)
//    }

    private fun performButtonClick() {
        Toast.makeText(this, "버튼 클릭됨!", Toast.LENGTH_SHORT).show()
    }
}