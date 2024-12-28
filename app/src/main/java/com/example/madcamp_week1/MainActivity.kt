package com.example.madcamp_week1

import android.os.Bundle
import android.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.madcamp_week1.databinding.ActivityMainBinding
import com.example.madcamp_week1.ui.MainViewPagerAdapter
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isUserInteraction = false // 사용자 상호작용 여부 플래그

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val viewPager = binding.viewPager

        // ViewPager 설정
        val adapter = MainViewPagerAdapter(this)
        viewPager.adapter = adapter

        // ViewPager2와 BottomNavigationView 연결
        navView.setOnItemSelectedListener { item ->
            isUserInteraction = true
            when (item.itemId) {
                R.id.navigation_home -> viewPager.currentItem = 0
                R.id.navigation_dashboard -> viewPager.currentItem = 1
                R.id.navigation_notifications -> viewPager.currentItem = 2
            }
            isUserInteraction = false
            true
        }


        // ViewPager 페이지 변경 시 BottomNavigationView 탭 변경
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (!isUserInteraction) { // 사용자 상호작용이 아닌 경우만 업데이트
                    when (position) {
                        0 -> navView.selectedItemId = R.id.navigation_home
                        1 -> navView.selectedItemId = R.id.navigation_dashboard
                        2 -> navView.selectedItemId = R.id.navigation_notifications
                    }
                }
            }
        })
    }
}