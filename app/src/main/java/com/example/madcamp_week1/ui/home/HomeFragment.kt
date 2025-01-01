package com.example.madcamp_week1

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.madcamp_week1.data.PersonData
import com.example.madcamp_week1.model.Person
import com.example.madcamp_week1.util.PreferenceHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private lateinit var adapter: PersonAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    private var isSearchActive: Boolean = false // 검색 상태를 추적
    private var currentSearchResults: List<Person> = emptyList()
    private lateinit var favoriteButton: ImageButton
    private var showingFavorites = false // 현재 즐겨찾기 필터 상태

    private var isBottomBarVisible = true // BottomNavigationView 상태 관리 변수

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

        adapter = PersonAdapter(sortedList, context,
            updateParentList = {
                // 현재 검색 상태를 반환
                if (isSearchActive) {
                    currentSearchResults
                } else {
                    PersonData.personListFile
                }
            },
            onPersonClick = { person ->
                // 클릭된 Person 객체로 다이얼로그 호출
                showPersonDialog(person)
            }
        )
        recyclerView.adapter = adapter

        // RecyclerView 스크롤 이벤트 감지 추가
        setupRecyclerViewScrollListener()

        // SearchView 초기화
        searchView = view.findViewById(R.id.search_view_home)
        setupSearchView()

        // 즐겨찾기 버튼 초기화
        favoriteButton = view.findViewById(R.id.favorite_button)
        setupFavoriteButton()
    }

    @SuppressLint("SetTextI18n")
    private fun showPersonDialog(person: Person) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_person, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // 다이얼로그에 Person 데이터 바인딩
        val imageView = dialogView.findViewById<ImageView>(R.id.dialog_image)
        Glide.with(this)
            .load(person.img)
            .placeholder(R.drawable.logo_white) // 로딩 중 표시할 기본 이미지
            .thumbnail(0.25f) // 원본 크기의 25% 크기 미리보기
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐싱 사용
            .into(imageView)

        dialogView.findViewById<TextView>(R.id.dialog_name).text = person.name
        dialogView.findViewById<TextView>(R.id.dialog_age).text = "생년월일: ${person.birth}"
        dialogView.findViewById<TextView>(R.id.dialog_party).text = "정당: ${person.party}"
        dialogView.findViewById<TextView>(R.id.dialog_tel).text = "전화번호: ${person.tel}"
        dialogView.findViewById<TextView>(R.id.dialog_office).text = "사무실: ${person.office}"
        dialogView.findViewById<TextView>(R.id.dialog_email).text = "이메일: ${person.email}"
        // dialogView.findViewById<TextView>(R.id.dialog_attendance).text = "출석률: ${person.attendance}%"

        // 정당 로고 설정
        val partyLogoView = dialogView.findViewById<ImageView>(R.id.dialog_party_logo)
        val partyLogoResId = getPartyLogo(person.party)
        if (partyLogoResId == null) {
            partyLogoView.visibility = View.GONE
        } else{
            partyLogoView.visibility = View.VISIBLE
            partyLogoView.setImageResource(partyLogoResId)
        }

        // GradientDrawable로 테두리 설정
        val partyColor = getPartyColor(person.party)
        val borderDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setStroke(8, partyColor) // 두께와 색상
            setColor(Color.TRANSPARENT) // 내부는 투명
        }

        dialogView.background = borderDrawable // 테두리 적용

        dialog.show()
    }

    private fun getPartyColor(partyName: String): Int {
        return when (partyName) {
            "더불어민주당" -> Color.parseColor("#0052A7")
            "국민의힘" -> Color.parseColor("#C9151E")
            "조국혁신당" -> Color.parseColor("#0033A0")
            "개혁신당" -> Color.parseColor("#FF7210")
            "진보당" -> Color.parseColor("#C70519")
            "기본소득당" -> Color.parseColor("#00C0B2")
            "사회민주당" -> Color.parseColor("#D37100")
            else -> Color.GRAY // 기본 색상
        }
    }

    private fun getPartyLogo(partyName: String): Int?{
        return when(partyName){
            "더불어민주당" -> R.drawable.minjudang
            "국민의힘" -> R.drawable.powerofkorea
            "조국혁신당" -> R.drawable.rebuildingkorea
            "개혁신당" -> R.drawable.reformkorea
            "진보당" -> R.drawable.jinbodang
            "기본소득당" -> R.drawable.basicincome_party
            "사회민주당" -> R.drawable.socialminjudang
            else -> null
        }
    }

    private fun setupRecyclerViewScrollListener() {
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.nav_view) // 부모 Activity의 BottomNavigationView

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤 방향에 따라 BottomNavigationView 감추기/보이기
                if (dy > 0 && isBottomBarVisible) { // 아래로 스크롤
                    bottomNavigationView?.let { hideBottomBar(it) }
                    isBottomBarVisible = false
                } else if (dy < 0 && !isBottomBarVisible) { // 위로 스크롤
                    bottomNavigationView?.let { showBottomBar(it) }
                    isBottomBarVisible = true
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        val mainActivity = activity as? MainActivity
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)

        bottomNavigationView?.let {
            if (mainActivity?.isBottomBarVisible == true) {
                // MainActivity 상태에 따라 BottomNavigationView를 표시
                isBottomBarVisible = true
                it.animate()
                    .translationY(0f)
                    .setDuration(200)
                    .start()
            } else {
                // MainActivity 상태에 따라 BottomNavigationView를 숨김
                isBottomBarVisible = false
                it.animate()
                    .translationY(it.height.toFloat())
                    .setDuration(200)
                    .start()
            }
        }
    }

    private fun hideBottomBar(bottomBar: BottomNavigationView) {
        if (bottomBar.translationY == 0f) { // 이미 숨겨져 있지 않은 경우만 애니메이션 수행
            bottomBar.animate()
                .translationY(bottomBar.height.toFloat())
                .setDuration(200)
                .withEndAction { isBottomBarVisible = false } // 상태 동기화
                .start()
        }
    }

    private fun showBottomBar(bottomBar: BottomNavigationView) {
        if (bottomBar.translationY != 0f) { // 이미 보이는 상태가 아닌 경우만 애니메이션 수행
            bottomBar.animate()
                .translationY(0f)
                .setDuration(200)
                .withEndAction { isBottomBarVisible = true } // 상태 동기화
                .start()
        }
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

            // 아이콘 변경
            if (showingFavorites) {
                favoriteButton.setImageResource(R.drawable.baseline_stars_24_checked) // 즐겨찾기 활성화 아이콘
            } else {
                favoriteButton.setImageResource(R.drawable.baseline_stars_24) // 즐겨찾기 비활성화 아이콘
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