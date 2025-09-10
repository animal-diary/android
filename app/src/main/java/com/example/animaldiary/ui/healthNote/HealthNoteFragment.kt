package com.example.animaldiary.ui.healthNote

import NoPetFragment
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
import com.example.animaldiary.ui.components.BottomSheetView
import com.google.android.material.bottomsheet.BottomSheetDialog

class HealthNoteFragment : Fragment(), NoPetFragment.OnPetAddedListener, NoRecordsFragment.OnAddRecordListener, HasRecordsFragment.OnRecordSelectedListener {

    private var _binding: FragmentHealthNoteBinding? = null
    private val binding get() = _binding!!

    // 프래그먼트 인스턴스를 클래스 변수로 선언
    private val noPetFragment = NoPetFragment()
    private val noRecordsFragment = NoRecordsFragment()
    private val hasRecordsFragment = HasRecordsFragment()

    // 현재 선택된 반려동물을 저장하는 변수 (상태 관리용)
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

        // 초기 프래그먼트들을 모두 추가하고, 첫 화면에 보일 프래그먼트만 보이게 설정
        if (!noPetFragment.isAdded) {
            childFragmentManager.beginTransaction()
                .add(R.id.fragment_container, noPetFragment, "noPet")
                .add(R.id.fragment_container, noRecordsFragment, "noRecords")
                .add(R.id.fragment_container, hasRecordsFragment, "hasRecords")
                .hide(noRecordsFragment)
                .hide(hasRecordsFragment)
                .commit()
        }

        // 초기 UI 상태 설정 (기록이 없는 상태로 시작)
        updateUi(false, false)

        // 반려동물 이름 레이아웃 클릭 리스너 추가
        binding.llPetName.setOnClickListener {
            showPetSelectionBottomSheet()
        }
    }

    private fun updateUi(hasPet: Boolean, hasRecords: Boolean) {
        if (hasPet) {
            // 반려동물이 등록된 경우
            binding.llPetName.isVisible = true
            binding.tvPetName.text = selectedPet?.name ?: "반려동물"

            if (hasRecords) {
                // 기록이 있을 경우
                childFragmentManager.beginTransaction()
                    .hide(noPetFragment)
                    .hide(noRecordsFragment)
                    .show(hasRecordsFragment)
                    .commit()
            } else {
                // 기록이 없을 경우
                childFragmentManager.beginTransaction()
                    .hide(noPetFragment)
                    .hide(hasRecordsFragment)
                    .show(noRecordsFragment)
                    .commit()
            }
        } else {
            // 반려동물이 등록되지 않은 경우
            binding.llPetName.isVisible = false
            // 반려동물이 없을 경우
            childFragmentManager.beginTransaction()
                .hide(noRecordsFragment)
                .hide(hasRecordsFragment)
                .show(noPetFragment)
                .commit()
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

        val adapter = PetListAdapter(petDataList, object : PetListAdapter.OnPetClickListener {
            override fun onPetClick(pet: Pet) {
                selectedPet = pet
                binding.tvPetName.text = selectedPet?.name
                // 기록이 있는지 확인하고 UI 업데이트 (더미 데이터에서는 항상 false)
                updateUi(true, false)
                dialog.dismiss()
            }
        }, initialSelectedPosition)
        recyclerView.adapter = adapter

        dialog.setContentView(sheet)
        dialog.show()
    }

    fun hideParentViews() {
        binding.topNavHealth.isVisible = false
        binding.llPetName.isVisible = false
    }

    fun showParentViews() {
        binding.topNavHealth.isVisible = true
        binding.llPetName.isVisible = true
    }

    override fun onAddPetClicked() {
        selectedPet = petDataList[0]
        updateUi(true, false)
    }

    override fun onAddRecordClicked() {
        updateUi(true, true)
    }

    override fun onRecordSelected(year: Int, month: Int, day: Int) {
        hideParentViews()

        // HealthRecordDetailFragment의 새 인스턴스를 생성
        val detailFragment = HealthRecordDetailFragment.newInstance(year, month, day)

        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}