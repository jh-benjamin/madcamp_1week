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
    private var spanCount: Int,
    private var spacing: Int, // 항목 간 간격
    private val onItemClick: (Person) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private var internalList: List<Person> = personList // 내부 리스트

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }

    fun updatespanCount(newSpanCount: Int, newSpacing: Int) {
        this.spanCount = newSpanCount
        this.spacing = newSpacing
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Person>) {
        internalList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = personList[position]

        // 이미지 로드
        Glide.with(holder.itemView.context)
            .load(person.img)
            .centerCrop()
            .into(holder.imageView)

        // 항목 크기 동적 조정
        val layoutParams = holder.imageView.layoutParams
        val parentWidth = holder.itemView.context.resources.displayMetrics.widthPixels

        // 분할별 간격 계산
        val totalSpacing = if (spanCount > 1) spacing * (spanCount - 1) else 0
        val itemWidth = (parentWidth - totalSpacing) / spanCount

        // 항목 크기 설정
        layoutParams.width = itemWidth
        layoutParams.height = (itemWidth * 1.5f).toInt() // 1:1.5 비율 유지
        holder.imageView.layoutParams = layoutParams

        // 항상 centerCrop으로 설정하여 공백 제거
        holder.imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        // 클릭 리스너 설정
        holder.itemView.setOnClickListener { onItemClick(person) }
    }
    override fun getItemCount(): Int = personList.size
}