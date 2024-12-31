package com.example.madcamp_week1.ui.notifications

import PartyInfoAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.R
import com.example.madcamp_week1.data.PartyInfoData
import com.example.madcamp_week1.data.PartyInfoData.partyList_22
import com.example.madcamp_week1.data.PartyInfoData.partyList_21
import com.example.madcamp_week1.data.PartyInfoData.partyList_20
import com.example.madcamp_week1.model.PartyInfo
import com.example.madcamp_week1.views.SemiCircleGraphView

class NotificationsFragment : Fragment() {

    private var isFirstSelection = true // Spinner 초기화 시 이벤트 방지 플래그
    private lateinit var partyAdapter: PartyInfoAdapter // RecyclerView 어댑터

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        // Spinner 설정
        val spinner: Spinner = view.findViewById(R.id.spinner_selection)
        val items = listOf("22대", "21대", "20대")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // RecyclerView 설정
        val partyInfoRecyclerView: RecyclerView = view.findViewById(R.id.party_info_recycler_view)
        partyInfoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        partyAdapter = PartyInfoAdapter(partyList_22) // 초기 데이터는 partyList_22로 설정
        partyInfoRecyclerView.adapter = partyAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SemiCircleGraphView 초기화
        val semiCircleGraphView = view.findViewById<SemiCircleGraphView>(R.id.semi_circle_graph_view)

        // Spinner 선택 이벤트 설정
        val spinner: Spinner = view.findViewById(R.id.spinner_selection)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFirstSelection) {
                    // 첫 선택 이벤트 무시
                    isFirstSelection = false
                } else {
                    // 선택 값 변경 시 애니메이션 실행
                    when (position) {
                        0 -> semiCircleGraphView.updatePartyList(PartyInfoData.partyList_22) // 22대
                        1 -> semiCircleGraphView.updatePartyList(PartyInfoData.partyList_21) // 21대
                        2 -> semiCircleGraphView.updatePartyList(PartyInfoData.partyList_20) // 20대
                    }

                    semiCircleGraphView?.startAnimation()
                }

                // RecyclerView 데이터 변경
                val selectedList = when (position) {
                    0 -> partyList_22 // "22대"
                    1 -> partyList_21 // "21대"
                    2 -> partyList_20 // "20대"
                    else -> partyList_22
                }
                updateRecyclerViewData(selectedList)
                val totalSeats = calculateTotalSeats(selectedList)
                updateTotalSeatsTextView(totalSeats)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않을 때는 처리하지 않음
                semiCircleGraphView.updatePartyList(PartyInfoData.partyList_22)
            }
        }
    }

    // RecyclerView 데이터 업데이트 메서드
    private fun updateRecyclerViewData(newData: List<PartyInfo>) {
        partyAdapter.updateData(newData)
    }

    private fun calculateTotalSeats(partyList: List<PartyInfo>): Int {
        return partyList.sumOf { it.seats }
    }

    private fun updateTotalSeatsTextView(totalSeats: Int) {
        val totalSeatsTextView: TextView = requireView().findViewById(R.id.textview_total_seats)
        totalSeatsTextView.text = "국회의원: ${totalSeats}석"
    }
}