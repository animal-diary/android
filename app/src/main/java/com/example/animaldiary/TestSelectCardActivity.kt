package com.example.animaldiary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.animaldiary.databinding.ActivityTestSelectCardBinding
import com.example.animaldiary.ui.components.SelectCardView

class TestSelectCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestSelectCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestSelectCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCards()
    }

    private fun setupCards() {
        val cards = listOf(
            binding.cardCenterIllust,
            binding.cardCenterNoIllust,
            binding.cardLeftIllust,
            binding.cardLeftNoIllust
        )

        cards.forEach { card ->
            card.setOnClickListener {
                val currentlySelected = card.isSelectedCard() // 현재 선택 상태 확인
                card.setSelectedCard(!currentlySelected)      // 토글
            }
        }

        // 초기에는 모두 선택 해제
        cards.forEach {
            it.setSelectedCard(false)
        }
    }
}