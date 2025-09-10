package com.example.animaldiary.ui.healthNote

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HasRecordsPagerAdapter(activity: HasRecordsFragment, private val tabTitles: List<String>) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = tabTitles.size

    override fun createFragment(position: Int): Fragment {
        // 각 탭에 해당하는 프래그먼트 생성
        return NoRecordsTabFragment.newInstance(tabTitles[position])
    }
}