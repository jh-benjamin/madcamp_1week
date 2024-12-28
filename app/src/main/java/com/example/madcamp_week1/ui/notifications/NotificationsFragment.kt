package com.example.madcamp_week1.ui.notification

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.R
import com.example.madcamp_week1.data.RegionSeatData
import com.example.madcamp_week1.model.RegionSeats
import com.example.madcamp_week1.ui.notifications.RegionSeatsAdapter
import android.widget.TextView
import android.widget.LinearLayout

class NotificationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        // 지도 클릭 이벤트 설정
        val mapImageView = view.findViewById<ImageView>(R.id.map_image)
        mapImageView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val x = event.x
                val y = event.y
                Log.d("MapClick", "Clicked at: x=$x, y=$y")
                handleMapClick(x, y)
            }
            true
        }
        return view
    }

    private fun handleMapClick(x: Float, y: Float) {
        val region = when {
            x in 310f..400f && y in 323f..380f -> "서울"
            x in 500f..600f && y in 500f..600f -> "부산"
            // 좌표 범위에 따라 추가
            else -> null
        }

        if (region != null) {
            val regionSeats = RegionSeatData.regionSeatsList.find { it.region == region }
            if (regionSeats != null) {
                showRegionSeats(regionSeats)
            } else {
                Log.d("MapClick", "Region not found: $region")
            }
        } else {
            Log.d("MapClick", "No region matched for coordinates: x=$x, y=$y")
        }
    }

    private fun showRegionSeats(regionSeats: RegionSeats) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_regionseats, null)

        // 지역명 설정
        val regionNameTextView = dialogView.findViewById<TextView>(R.id.tv_region_name)
        regionNameTextView.text = regionSeats.region

        // 정당 리스트 동적 추가
        val partyListContainer = dialogView.findViewById<LinearLayout>(R.id.party_list_container)
        partyListContainer.removeAllViews()

        regionSeats.parties.forEach { party ->
            val partyTextView = TextView(requireContext()).apply {
                text = "${party.partyName}: ${party.seats}석"
                textSize = 16f
                setPadding(0, 8, 0, 8)
            }
            partyListContainer.addView(partyTextView)
        }

        // 다이얼로그 표시
        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("확인", null)
            .show()
    }
}