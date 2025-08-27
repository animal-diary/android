package com.example.animaldiary.test.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.animaldiary.R

class CalendarAdapter(val days: List<CalendarDay>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    // 선택된 날짜들의 위치를 저장하는 Set
    private val selectedPositions = mutableSetOf<Int>()

    // 아이템 클릭 이벤트를 위한 인터페이스
    interface OnItemClickListener {
        fun onItemClick(positions: Set<Int>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_date, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = days[position]
        holder.bind(day, selectedPositions.contains(position))
    }

    override fun getItemCount(): Int = days.size

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayTextView: TextView = itemView.findViewById(R.id.tv_date)

        fun bind(day: CalendarDay, isSelected: Boolean) {
            dayTextView.text = day.day.toString()
            // 현재 달이 아닌 날짜는 색을 흐릿하게
            if (day.isCurrentMonth)
                dayTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.fg_neutral_primary))
            else
                dayTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.fg_disabled))

            // 선택 여부에 따라 배경과 텍스트 색상을 변경
            if (isSelected) {
                dayTextView.setBackgroundResource(R.drawable.bg_calendar_none_pressed) // 선택된 날짜 배경
//                dayTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.fg_inverse)) // 선택된 날짜 텍스트 색상
            } else {
                dayTextView.background = null // 배경 제거
//                dayTextView.setTextColor(
//                    ContextCompat.getColor(
//                        itemView.context,
//                        if (day.isToday) R.color.fg_selected else R.color.fg_neutral_primary
//                    )
//                )
            }

            // 클릭 리스너를 설정하여 클릭 이벤트 전달
            itemView.setOnClickListener {
                if (day.isCurrentMonth) {
                    val position = adapterPosition
                    if (selectedPositions.contains(position)) {
                        // 이미 선택된 경우, 선택 해제
                        selectedPositions.remove(position)
                    } else {
                        // 선택되지 않은 경우, 새로 추가
                        selectedPositions.add(position)
                    }
                    notifyItemChanged(position)
                    itemClickListener.onItemClick(selectedPositions)
                }
            }
        }
    }
}