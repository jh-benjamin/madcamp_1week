package com.example.madcamp_week1.ui.dashboard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.R
import com.example.madcamp_week1.data.PersonData.personListFile
import com.example.madcamp_week1.model.Person

class DashboardFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val personList = personListFile

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_dashboard)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = GalleryAdapter(personList) { person ->
            showPersonDialog(person)
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun showPersonDialog(person: Person) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_person, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        // 다이얼로그에 Person 데이터 바인딩
        dialogView.findViewById<ImageView>(R.id.dialog_image).setImageResource(person.imageResId)
        dialogView.findViewById<TextView>(R.id.dialog_name).text = person.name
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
            else -> null
        }
    }

    private fun getPartyColor(partyName: String): Int {
        return when (partyName) {
            "더불어민주당" -> Color.parseColor("#0052A7")
            "국민의힘" -> Color.parseColor("#C9151E")
            "조국혁신당" -> Color.parseColor("#0033A0")
            "개혁신당" -> Color.parseColor("#FF7210")
            else -> Color.GRAY // 기본 색상
        }
    }

}