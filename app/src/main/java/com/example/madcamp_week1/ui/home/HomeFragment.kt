package com.example.madcamp_week1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.data.PersonData
import com.example.madcamp_week1.model.Person
import com.example.madcamp_week1.util.PreferenceHelper

class HomeFragment : Fragment() {

    private lateinit var adapter: PersonAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    private var isSearchActive: Boolean = false // 검색 상태를 추적
    private var currentSearchResults: List<Person> = emptyList()
    private lateinit var favoriteButton: Button
    private var showingFavorites = false // 현재 즐겨찾기 필터 상태

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

        // 데이터 로드 시 isFavorite 기준 -> 이름 오름차순 정렬
        val sortedList = PersonData.personListFile
            .sortedWith(compareByDescending<Person> {
                PreferenceHelper(context).isFavorite(it.name) // SharedPreferences에서 favorite 상태 로드
            }.thenBy { it.name }) // 이름 오름차순 정렬

        // 어댑터 초기화
        adapter = PersonAdapter(sortedList, context) {
            // 현재 검색 상태를 반환
            if (isSearchActive) {
                currentSearchResults
            } else {
                PersonData.personListFile
            }
        }
        recyclerView.adapter = adapter

        // SearchView 초기화
        searchView = view.findViewById(R.id.search_view_home)
        setupSearchView()

        // 즐겨찾기 버튼 초기화
        favoriteButton = view.findViewById(R.id.favorite_button)
        setupFavoriteButton()
    }

    private fun setupFavoriteButton() {
        favoriteButton.setOnClickListener {
            showingFavorites = !showingFavorites // 즐겨찾기 필터 상태 토글

            val filteredList = if (showingFavorites) {
                if (isSearchActive) {
                    // 검색 상태에서 즐겨찾기 필터 적용
                    currentSearchResults.filter {
                        PreferenceHelper(requireContext()).isFavorite(it.name)
                    }.sortedBy { it.name } // 이름 오름차순 정렬
                } else {
                    // 전체 데이터에서 즐겨찾기 필터 적용
                    PersonData.personListFile.filter {
                        PreferenceHelper(requireContext()).isFavorite(it.name)
                    }.sortedBy { it.name }
                }
            } else {
                if (isSearchActive) {
                    // 검색 상태 유지 (즐겨찾기 비활성화)
                    currentSearchResults.sortedWith(compareByDescending<Person> {
                        PreferenceHelper(requireContext()).isFavorite(it.name)
                    }.thenBy { it.name })
                } else {
                    // 전체 데이터 표시
                    PersonData.personListFile.sortedWith(compareByDescending<Person> {
                        PreferenceHelper(requireContext()).isFavorite(it.name)
                    }.thenBy { it.name })
                }
            }

            adapter.updateList(filteredList)

            // 버튼 텍스트 색상 변경
            if (showingFavorites) {
                favoriteButton.setTextColor(resources.getColor(R.color.yellow, null))
            } else {
                favoriteButton.setTextColor(resources.getColor(R.color.white, null))
            }
        }
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
            isSearchActive = true // 검색 상태 활성화
            currentSearchResults = PersonData.personListFile.filter {
                it.name.contains(query, ignoreCase = true) || it.party.startsWith(query, ignoreCase = true)
            }
            val filteredList = currentSearchResults.filter {
                // 즐겨찾기 필터가 활성화된 경우 추가 필터링
                !showingFavorites || PreferenceHelper(requireContext()).isFavorite(it.name)
            }.sortedWith(compareByDescending<Person> {
                PreferenceHelper(requireContext()).isFavorite(it.name)
            }.thenBy { it.name })
            adapter.updateList(filteredList)
        } else {
            isSearchActive = false // 검색 상태 비활성화
            resetList()
        }
    }

    private fun resetList() {
        isSearchActive = false // 검색 상태 초기화
        currentSearchResults = PersonData.personListFile // 검색 결과 초기화
        val sortedList = currentSearchResults
            .sortedWith(compareByDescending<Person> {
                PreferenceHelper(requireContext()).isFavorite(it.name)
            }.thenBy { it.name }) // 기본 정렬
        adapter.updateList(sortedList)
    }
}