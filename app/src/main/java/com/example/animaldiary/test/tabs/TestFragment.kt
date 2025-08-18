package com.example.animaldiary.test.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.animaldiary.R

class TestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_test, container, false)
        val tabTitle = arguments?.getString("TAB_TITLE") ?: "기본 탭"
        view.findViewById<TextView>(R.id.fragment_text).text = tabTitle
        return view
    }

    companion object {
        fun newInstance(title: String): TestFragment {
            val fragment = TestFragment()
            val args = Bundle()
            args.putString("TAB_TITLE", title)
            fragment.arguments = args
            return fragment
        }
    }
}