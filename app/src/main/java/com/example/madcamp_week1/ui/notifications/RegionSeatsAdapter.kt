package com.example.madcamp_week1.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.R
import com.example.madcamp_week1.model.RegionSeats

class RegionSeatsAdapter(
    private val regionSeatsList: List<RegionSeats>,
    private val onItemClick: (RegionSeats) -> Unit // 클릭 이벤트 전달
) : RecyclerView.Adapter<RegionSeatsAdapter.RegionSeatsViewHolder>() {

    // ViewHolder 정의
    class RegionSeatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val regionNameTextView: TextView = itemView.findViewById(R.id.tv_region_name)
        val partyListContainer: LinearLayout = itemView.findViewById(R.id.party_list_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionSeatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dialog_regionseats, parent, false)
        return RegionSeatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegionSeatsViewHolder, position: Int) {
        val regionSeats = regionSeatsList[position]

        // 지역명 설정
        holder.regionNameTextView.text = regionSeats.region

        // 기존 정당 리스트 초기화
        holder.partyListContainer.removeAllViews()

        // 정당 리스트 동적 추가
        regionSeats.parties.forEach { party ->
            val partyTextView = TextView(holder.itemView.context).apply {
                text = "${party.partyName}: ${party.seats}석"
                textSize = 16f
                setPadding(0, 8, 0, 8)
            }
            holder.partyListContainer.addView(partyTextView)
        }

        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            onItemClick(regionSeats) // 클릭된 데이터를 Fragment로 전달
        }
    }

    override fun getItemCount(): Int = regionSeatsList.size
}
