package com.example.madcamp_week1

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.madcamp_week1.data.PersonData
import com.example.madcamp_week1.databinding.ActivityMainBinding
import com.example.madcamp_week1.ui.MainViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var isBottomBarVisible = true // 중앙에서 관리하는 상태 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // JSON 데이터 로드
        PersonData.initializeData(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 숨기기

        val navView: BottomNavigationView = binding.navView
        val viewPager = binding.viewPager

        // ViewPager 설정
        val adapter = MainViewPagerAdapter(this)
        viewPager.adapter = adapter

        // BottomNavigationView와 ViewPager 연결
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    viewPager.currentItem = 0
                    showBottomBar()
                }
                R.id.navigation_dashboard -> {
                    viewPager.currentItem = 1
                    showBottomBar()
                }
                R.id.navigation_notifications -> {
                    viewPager.currentItem = 2
                    showBottomBar()
                }
            }
            true
        }

        // ViewPager 페이지 변경 시 BottomNavigationView 상태 업데이트
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        navView.selectedItemId = R.id.navigation_home
                        showBottomBar()
                    }
                    1 -> {
                        navView.selectedItemId = R.id.navigation_dashboard
                        showBottomBar()
                    }
                    2 -> {
                        navView.selectedItemId = R.id.navigation_notifications
                        showBottomBar()
                    }
                }
            }
        })
    }

    // BottomNavigationView 숨기기
    private fun hideBottomBar() {
        val bottomBar = binding.navView
        if (isBottomBarVisible) {
            bottomBar.animate()
                .translationY(bottomBar.height.toFloat())
                .setDuration(200)
                .withEndAction { isBottomBarVisible = false } // 상태 동기화
                .start()
        }
    }

    // BottomNavigationView 표시하기
    private fun showBottomBar() {
        val bottomBar = binding.navView
        if (!isBottomBarVisible) {
            bottomBar.animate()
                .translationY(0f)
                .setDuration(200)
                .withEndAction { isBottomBarVisible = true } // 상태 동기화
                .start()
        }
    }
}