package com.example.madcamp_week1.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.R
import com.example.madcamp_week1.model.Person

class GalleryAdapter(
    private val personList: List<Person>,
    private val onItemClick: (Person) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)

        fun bind(person: Person) {
            imageView.setImageResource(person.imageResId)
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