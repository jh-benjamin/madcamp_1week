package com.example.madcamp_week1

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.data.PersonData
import com.example.madcamp_week1.model.Person

class PersonAdapter(private val personList: List<Person>) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.person_name)
        val partyTextView: TextView = itemView.findViewById(R.id.person_party)
        val phoneTextView: TextView = itemView.findViewById(R.id.person_phone)
        val imageView: ImageView = itemView.findViewById(R.id.person_image)
        val favoriteStar: ImageView = itemView.findViewById(R.id.favorite_star)
        val phoneIcon: ImageView = itemView.findViewById(R.id.phone_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]
        holder.nameTextView.text = person.name
        holder.partyTextView.text = person.party
        holder.phoneTextView.text = person.tel // 전화번호 추가
        holder.imageView.setImageResource(R.drawable.ahncheolsoo)

        // 초기 상태 설정
        val isFavorite = person.isFavorite
        updateStarIcon(holder.favoriteStar, isFavorite)

        // 클릭 이벤트 설정
        holder.favoriteStar.setOnClickListener {
            person.isFavorite = !person.isFavorite // 선호도 상태 토글
            updateStarIcon(holder.favoriteStar, person.isFavorite)
        }
        // 전화기 아이콘 클릭 이벤트
        holder.phoneIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL) // 전화 다이얼러 앱 호출
            intent.data = Uri.parse("tel:${person.tel}")
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    private fun updateStarIcon(starView: ImageView, isFavorite: Boolean) {
        if (isFavorite) {
            starView.setImageResource(R.drawable.ic_star_filled) // 채워진 별
        } else {
            starView.setImageResource(R.drawable.ic_star_outline) // 빈 별
        }
    }
}