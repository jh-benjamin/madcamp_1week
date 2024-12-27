package com.example.madcamp_week1.ui.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.R
import com.example.madcamp_week1.data.PersonData.personListFile
import com.example.madcamp_week1.databinding.FragmentDashboardBinding
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

        dialog.show()
    }
}