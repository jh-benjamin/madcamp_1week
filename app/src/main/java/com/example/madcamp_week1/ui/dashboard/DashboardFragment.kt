package com.example.madcamp_week1.ui.dashboard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.R
import com.example.madcamp_week1.data.PersonData.personListFile
import com.example.madcamp_week1.model.Person
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.madcamp_week1.MainActivity

class DashboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter

    private var isBottomBarVisible = true // 바텀바 상태 관리 변수

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_dashboard)
        setupRecyclerView(2) // 초기 상태: 2분할

        // 버튼 초기화 및 클릭 리스너 설정
        val buttonOneSplit = view.findViewById<Button>(R.id.button_one_split)
        val buttonTwoSplit = view.findViewById<Button>(R.id.button_two_split)
        val buttonFourSplit = view.findViewById<Button>(R.id.button_four_split)

        buttonOneSplit.setOnClickListener { setupRecyclerView(1) }
        buttonTwoSplit.setOnClickListener { setupRecyclerView(2) }
        buttonFourSplit.setOnClickListener { setupRecyclerView(4) }

        // 스크롤 이벤트 감지 추가
        setupRecyclerViewScrollListener(recyclerView)

        return view
    }

    private fun setupRecyclerView(spanCount: Int) {
        val spacing = 0
        recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)

        // 기존 ItemDecoration 제거
        while (recyclerView.itemDecorationCount > 0) {
            recyclerView.removeItemDecorationAt(0)
        }

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        })

        // 데이터를 백그라운드 스레드에서 셔플
        Thread {
            val shuffledList = personListFile.shuffled()

            activity?.runOnUiThread {
                if (!::adapter.isInitialized) {
                    adapter = GalleryAdapter(shuffledList, spanCount, spacing) { person ->
                        showPersonDialog(person)
                    }
                    recyclerView.adapter = adapter
                } else {
                    (recyclerView.adapter as GalleryAdapter).updateList(shuffledList)
                    (recyclerView.adapter as GalleryAdapter).updatespanCount(spanCount, spacing)
                }
            }
        }.start()
    }

    private fun setupRecyclerViewScrollListener(recyclerView: RecyclerView) {
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

}