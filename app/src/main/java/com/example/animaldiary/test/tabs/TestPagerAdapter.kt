package com.example.animaldiary.test.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TestPagerAdapter(activity: FragmentActivity, private val tabTitles: List<String>) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = tabTitles.size

    override fun createFragment(position: Int): Fragment {
        // 각 탭에 해당하는 프래그먼트 생성
        return TestFragment.newInstance(tabTitles[position])
    }
}