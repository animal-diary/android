package com.example.animaldiary.ui.healthNote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.animaldiary.R
import com.example.animaldiary.ui.components.SelectCardView

class PetListAdapter(
    private val petList: List<Pet>,
    private val onPetClickListener: OnPetClickListener,
    private var selectedPosition: Int
) : RecyclerView.Adapter<PetListAdapter.PetViewHolder>() {

    interface OnPetClickListener {
        fun onPetClick(pet: Pet)
    }

    class PetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val selectCardView: SelectCardView = view.findViewById(R.id.selectCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet_selection, parent, false) // 새로운 레이아웃 파일
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = petList[position]
        holder.selectCardView.setScTitle(pet.name)
        holder.selectCardView.setScDescription(pet.description)

        // 선택 상태에 따라 배경 변경
        val isSelected = position == selectedPosition
        holder.selectCardView.setSelectedCard(isSelected)

        // 클릭 리스너 설정
        holder.selectCardView.setOnClickListener {
            // 이전에 선택된 아이템의 위치를 저장
            val previousSelectedPosition = selectedPosition

            // 현재 클릭된 아이템의 위치를 업데이트
            selectedPosition = holder.layoutPosition

            // 이전에 선택된 아이템과 현재 선택된 아이템의 뷰를 업데이트
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)

            // 클릭된 아이템의 반려동물 정보를 전달
            onPetClickListener.onPetClick(pet)
        }
    }

    override fun getItemCount() = petList.size
}