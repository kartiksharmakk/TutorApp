package com.example.myapplication.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.Student.AttemptedTestsFragment
import com.example.myapplication.Student.PendingTestsFragment

class MyPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> PendingTestsFragment()
            1 -> AttemptedTestsFragment()
            else -> PendingTestsFragment()
        }
    }

}