package com.example.animaldiary

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.animaldiary.ui.components.TextAreaView

class TestTextAreaActivity : AppCompatActivity() {

    private lateinit var textArea: TextAreaView
    private lateinit var btnRead: Button
    private lateinit var btnDisable: Button
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_text_area)

        // 뷰 참조
        textArea = findViewById(R.id.testTextArea)
        btnRead = findViewById(R.id.btnRead)
        btnDisable = findViewById(R.id.btnDisable)
        btnReset = findViewById(R.id.btnReset)

        // 버튼 클릭 시 현재 입력값을 토스트로 확인
        btnRead.setOnClickListener {
            clearTextAreaFocus()

            val input = textArea.getText()
            Toast.makeText(this, "입력값: $input", Toast.LENGTH_SHORT).show()
        }

        // 비활성화 테스트
        btnDisable.setOnClickListener {
            textArea.setDisabledState()
        }

        // 초기화 테스트
        btnReset.setOnClickListener {
            textArea.setTitle("내용을 입력해주세요")
            textArea.setDescription("200자 이내로 작성해 주세요")
            textArea.setMaxLength(200)
            textArea.showRequired(true)
            textArea.showCount(true)
            textArea.setEnabled(true)
            textArea.setDefaultState()
        }
    }

    // 화면 터치 시 EditText 외부 영역 클릭 판별하여 포커스 해제
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            currentFocus?.let { v ->
                if (v is EditText) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        clearTextAreaFocus()
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun clearTextAreaFocus() {
        textArea.clearFocus()
        // 키보드 숨기기
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(textArea.windowToken, 0)
    }
}
