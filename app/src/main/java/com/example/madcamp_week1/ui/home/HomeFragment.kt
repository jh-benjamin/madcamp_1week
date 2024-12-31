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

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recycler_view_home)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        if (PersonData.personListFile.isEmpty()) {
            Log.e("HomeFragment", "PersonData.personListFile is empty. Check your data initialization.")
        }
        adapter = PersonAdapter(PersonData.personListFile) // 초기 데이터를 어댑터에 전달
        recyclerView.adapter = adapter

        // SearchView 초기화
        searchView = view.findViewById(R.id.search_view_home)
        setupSearchView()
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("textsubmit", "onQueryTextSubmit called with query: $query")
                if (!query.isNullOrEmpty()) {
                    val filteredList = PersonData.personListFile.filter { it.name.startsWith(query, ignoreCase = true) }
                    adapter.updateList(filteredList)
                } else {
                    adapter.updateList(PersonData.personListFile) // 초기 데이터로 복원
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("textchange", "onQueryTextChange called with newText: $newText")
                val filteredList = if (!newText.isNullOrEmpty()) {
                    PersonData.personListFile.filter { it.name.startsWith(newText, ignoreCase = true) }
                } else {
                    PersonData.personListFile // 초기 데이터로 복원
                }
                adapter.updateList(filteredList)
                return true
            }
        })

        searchView.setOnCloseListener {
            // 초기 상태로 복원
            adapter.updateList(PersonData.personListFile) // 초기 데이터로 복원
            searchView.setQuery("", false) // 검색창 텍스트 초기화
            searchView.clearFocus() // 포커스 해제
            false // 기본 동작 유지
        }
    }
}