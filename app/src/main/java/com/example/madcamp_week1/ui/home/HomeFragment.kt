package com.example.madcamp_week1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.data.PersonData
import com.example.madcamp_week1.model.Person

class HomeFragment : Fragment() {

    private lateinit var adapter: PersonAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext() // 한 번 호출 후 재사용

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recycler_view_home)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // PersonData가 비어있는지 확인
        if (PersonData.personListFile.isEmpty()) {
            Log.e("HomeFragment", "PersonData.personListFile is empty. Initialize data first.")
        }

        // 어댑터 초기화
        adapter = PersonAdapter(PersonData.personListFile, context)
        recyclerView.adapter = adapter

        // SearchView 초기화
        searchView = view.findViewById(R.id.search_view_home)
        setupSearchView()
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼 클릭 시 필터링 실행
                filterList(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 텍스트 변경 시 실시간 필터링
                filterList(newText)
                return true
            }
        })

        searchView.setOnCloseListener {
            // 검색창 닫을 때 초기화
            resetList()
            false
        }
    }

    private fun filterList(query: String?) {
        if (!query.isNullOrEmpty()) {
            val filteredList = PersonData.personListFile.filter {
                it.name.contains(query, ignoreCase = true) || it.party.startsWith(query, ignoreCase = true)
            }
            adapter.updateList(filteredList)
        } else {
            resetList()
        }
    }

    private fun resetList() {
        // 초기 데이터로 복원
        adapter.updateList(PersonData.personListFile)
    }
}