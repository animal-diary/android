package com.example.animaldiary.ui.healthNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animaldiary.R
import com.example.animaldiary.databinding.FragmentHealthNoteBinding
import com.example.animaldiary.ui.components.ActionButtonView
import com.example.animaldiary.ui.components.BottomSheetView
import com.google.android.material.bottomsheet.BottomSheetDialog

class HealthNoteFragment : Fragment() {

    private var _binding: FragmentHealthNoteBinding? = null
    private val binding get() = _binding!!

    private val hasPet = false
    private val hasRecords = false

    // 현재 선택된 반려동물을 저장하는 변수
    private var selectedPet: Pet? = null

    // 더미 데이터 (실제로는 데이터베이스에서 가져와야 함)
    private val petDataList = listOf(
        Pet("1", "빵뽕이", "심혈관계, 호흡계, 비뇨기계, 골격계, 피부계, 신경계, 소화기계, 내분비계"),
        Pet("2", "초코송이 초코", "심혈관계, 호흡계, 비뇨기계, 골격계, 피부계, 신경계, 소화기계, 내분비계")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기 UI 상태 설정
        updateUi(hasPet, hasRecords)

        val btnAddPet = binding.clNoPet.findViewById<ActionButtonView>(R.id.btn_add_pet)

        // "반려동물 추가하기" 버튼 클릭 리스너 설정
        btnAddPet.setOnClickListener {
            // 버튼 클릭 시 반려동물이 등록된 상태로 가정하고 UI 업데이트
            // 첫 번째 반려동물을 기본으로 선택
            selectedPet = petDataList[0]
            updateUi(true, false)
        }

        // ll_pet_name 클릭 리스너 설정
        binding.llPetName.setOnClickListener {
            showPetSelectionBottomSheet()
        }

    }

    private fun updateUi(hasPet: Boolean, hasRecords: Boolean) {
        if (hasPet) {
            // 반려동물이 등록된 경우 (왼쪽 이미지)
            binding.llPetName.isVisible = true
            binding.llHealthNote.isVisible = false
            binding.tvPetName.text = selectedPet?.name ?: "반려동물" // 선택된 반려동물의 이름 표시

            if (hasRecords) {
                // 기록이 있을 경우
                binding.clNoRecords.isVisible = false
                binding.clNoPet.isVisible = false
                // TODO: 기록이 있을 때의 뷰를 visible로 설정하는 로직 추가
            } else {
                // 기록이 없을 경우 (왼쪽 이미지의 빈 화면)
                binding.clNoRecords.isVisible = true
                binding.clNoPet.isVisible = false
            }
        } else {
            // 반려동물이 등록되지 않은 경우 (오른쪽 이미지)
            binding.llPetName.isVisible = false
            binding.llHealthNote.isVisible = true
            binding.clNoRecords.isVisible = false
            binding.clNoPet.isVisible = true
        }
    }

    // 바텀시트를 표시하는 함수
    private fun showPetSelectionBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val sheet = BottomSheetView(requireContext()).apply {
            setTitle("아이 선택하기")
            setLeftIcon(R.drawable.ic_x) {
                dialog.dismiss()
            }
            setHandleVisible(true)
        }

        val bottomSheetContent = layoutInflater.inflate(R.layout.bottom_sheet_content_pet_selection, sheet, false)
        sheet.setContent(bottomSheetContent)

        // RecyclerView 설정
        val recyclerView = bottomSheetContent.findViewById<RecyclerView>(R.id.rv_pet_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val initialSelectedPosition = petDataList.indexOfFirst { it.id == selectedPet?.id }

        // 어댑터 연결
        val adapter = PetListAdapter(petDataList, object : PetListAdapter.OnPetClickListener {
            override fun onPetClick(pet: Pet) {
                selectedPet = pet
                binding.tvPetName.text = selectedPet?.name // 클릭된 반려동물의 이름으로 텍스트뷰 업데이트
                dialog.dismiss() // 바텀시트 닫기
            }
        }, initialSelectedPosition)
        recyclerView.adapter = adapter

        dialog.setContentView(sheet)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
