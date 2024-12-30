package com.example.madcamp_week1.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcamp_week1.R
import com.example.madcamp_week1.model.Person

class GalleryAdapter(
    private val personList: List<Person>,
    private val onItemClick: (Person) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)

        fun bind(person: Person) {
            // Glide를 사용하여 URL에서 이미지를 동적으로 로드
                Glide.with(itemView.context)
                .load(person.img)  // person.img는 JSON에서 제공된 사진 URL입니다.
                //.placeholder(R.drawable.logo_white)  // 로딩 중 기본 이미지
                .error(R.drawable.errorimage)  // 로드 실패 시 오류 이미지
                .into(imageView)

            // 항목 클릭 시 콜백 호출
            itemView.setOnClickListener { onItemClick(person) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(personList[position])
    }

    override fun getItemCount(): Int = personList.size

}