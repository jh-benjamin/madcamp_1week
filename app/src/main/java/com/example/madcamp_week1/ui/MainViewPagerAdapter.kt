package com.example.madcamp_week1.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.madcamp_week1.HomeFragment
import com.example.madcamp_week1.ui.dashboard.DashboardFragment
import com.example.madcamp_week1.ui.notifications.NotificationsFragment

class MainViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 3 // 페이지 개수
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> DashboardFragment()
            2 -> NotificationsFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}