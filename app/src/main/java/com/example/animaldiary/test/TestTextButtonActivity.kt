package com.example.animaldiary.test

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.animaldiary.databinding.ActivityTestTextButtonBinding
import com.example.animaldiary.ui.components.TextButtonView

class TestTextButtonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestTextButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestTextButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTextButtons()
    }

    private fun setupTextButtons() {
        // Primary 버튼
        binding.testTextButtonPrimary.setOnClickListener {
            Toast.makeText(this, "Primary 버튼 클릭!", Toast.LENGTH_SHORT).show()
        }

        // Neutral 버튼
        binding.testTextButtonNeutral.setOnClickListener {
            Toast.makeText(this, "Neutral 버튼 클릭!", Toast.LENGTH_SHORT).show()
        }

        // 테스트: 3초 후 Primary 버튼 비활성화, 6초 후 다시 활성화
        binding.testTextButtonPrimary.postDelayed({
            binding.testTextButtonPrimary.setEnabled(false)
            Toast.makeText(this, "Primary 버튼 비활성화됨", Toast.LENGTH_SHORT).show()

            binding.testTextButtonPrimary.postDelayed({
                binding.testTextButtonPrimary.setEnabled(true)
                Toast.makeText(this, "Primary 버튼 활성화됨", Toast.LENGTH_SHORT).show()
            }, 3000)
        }, 3000)
    }
}