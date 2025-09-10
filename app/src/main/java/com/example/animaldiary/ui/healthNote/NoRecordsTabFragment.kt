package com.example.animaldiary.ui.healthNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.animaldiary.R

class NoRecordsTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_no_records_tab, container, false)
//        val tabTitle = arguments?.getString("TAB_TITLE") ?: "기본 탭"
//        view.findViewById<TextView>(R.id.fragment_text).text = tabTitle
        return view
    }

    companion object {
        fun newInstance(title: String): NoRecordsTabFragment {
            val fragment = NoRecordsTabFragment()
            val args = Bundle()
            args.putString("TAB_TITLE", title)
            fragment.arguments = args
            return fragment
        }
    }
}