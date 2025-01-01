package com.example.madcamp_week1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.madcamp_week1.data.PersonData
import com.example.madcamp_week1.model.Person
import com.example.madcamp_week1.util.PreferenceHelper // 추가된 PreferenceHelper import

class PersonAdapter(
    private var personList: List<Person>,
    private val context: Context,
    private val updateParentList: () -> List<Person>, // 부모에서 현재 상태를 가져오는 함수
    private val onPersonClick: (Person) -> Unit // 항목 클릭 시 호출할 콜백 추가
) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    private val preferenceHelper = PreferenceHelper(context) // SharedPreferences 초기화

    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.person_name)
        val partyTextView: TextView = itemView.findViewById(R.id.person_party)
        val phoneTextView: TextView = itemView.findViewById(R.id.person_phone)
        val imageView: ImageView = itemView.findViewById(R.id.person_image)
        val favoriteStar: ImageView = itemView.findViewById(R.id.favorite_star)
        val phoneIcon: ImageView = itemView.findViewById(R.id.phone_icon)
    }

    fun updateList(newList: List<Person>) {
        personList = newList
        notifyDataSetChanged() // 데이터 갱신
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
        holder.phoneTextView.text = person.tel

        Log.d("onBindViewHolder", "Binding person: ${person.name}")

        // SharedPreferences에서 상태 로드
        person.isFavorite = preferenceHelper.isFavorite(person.name)

        // 정당 색상 매핑
        val partyColorMap = mapOf(
            "더불어민주당" to R.color.더불어민주당,
            "국민의힘" to R.color.국민의힘,
            "조국혁신당" to R.color.조국혁신당,
            "개혁신당" to R.color.개혁신당,
            "진보당" to R.color.진보당,
            "기본소득당" to R.color.기본소득당,
            "사회민주당" to R.color.사회민주당,
            "무소속" to R.color.무소속
        )

        // 정당에 따른 텍스트 색상 설정
        val colorRes = partyColorMap[person.party] ?: R.color.무소속
        holder.partyTextView.setTextColor(holder.itemView.context.getColor(colorRes))

        Glide.with(holder.itemView.context)
            .load(person.img)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐싱 사용
            .error(R.drawable.errorimage)
            .into(holder.imageView)

        // 아이콘 상태 업데이트
        updateStarIcon(holder.favoriteStar, person.isFavorite)

        // 클릭 이벤트 설정: 선호도 상태 저장
        holder.favoriteStar.setOnClickListener {
            person.isFavorite = !person.isFavorite // 상태 토글
            updateStarIcon(holder.favoriteStar, person.isFavorite)
            preferenceHelper.setFavorite(person.name, person.isFavorite) // 상태 저장

            // 부모의 현재 검색 상태 기준으로 정렬 및 갱신
            val updatedList = updateParentList()
                .sortedWith(compareByDescending<Person> { it.isFavorite }.thenBy { it.name })
            updateList(updatedList)
        }

        // 전화기 아이콘 클릭 이벤트
        holder.phoneIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${person.tel}")
            holder.itemView.context.startActivity(intent)
        }

        // **항목 클릭 이벤트 추가**
        holder.itemView.setOnClickListener {
            onPersonClick(person) // 클릭된 Person 객체를 콜백으로 전달
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

    private fun refreshList() {
        // 현재 리스트를 isFavorite 기준으로 정렬
        val sortedList = PersonData.personListFile
            .sortedWith(compareByDescending<Person> { it.isFavorite }.thenBy { it.name })
        updateList(sortedList) // 어댑터 업데이트
    }
}

