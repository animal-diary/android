package com.example.animaldiary

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.animaldiary.ui.components.TextInputView

class TestTextInputActivity : AppCompatActivity() {

    private lateinit var inputEmail: TextInputView
    private lateinit var btnValidate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_text_input)

        inputEmail = findViewById(R.id.inputEmail)
        btnValidate = findViewById(R.id.btnValidate)

        btnValidate.setOnClickListener {
            val email = inputEmail.getText()

            if (email.isBlank()) {
                inputEmail.setError("이메일을 입력해주세요")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                inputEmail.setError("이메일 형식이 아닙니다")
            } else {
                inputEmail.clearError()
                Toast.makeText(this, "입력된 이메일: $email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
