package com.example.animaldiary.ui.healthNote

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.animaldiary.databinding.FragmentNoRecordsBinding

class NoRecordsFragment : Fragment() {

    private var _binding: FragmentNoRecordsBinding? = null
    private val binding get() = _binding!!

    private var onAddRecordListener: OnAddRecordListener? = null

    interface OnAddRecordListener {
        fun onAddRecordClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnAddRecordListener) {
            onAddRecordListener = parentFragment as OnAddRecordListener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoRecordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 기록 추가하기 버튼 클릭 리스너 설정
        binding.btnAddRecord.setOnClickListener {
            onAddRecordListener?.onAddRecordClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}