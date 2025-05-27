package com.example.teamup.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.CardEvent
import com.example.teamup.Person
import com.example.teamup.R

class PeopleAdapter (
    private val items: List<Person>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PeopleAdapter.CardViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Person)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.list_item_text)
        val itemIcon: ImageView = itemView.findViewById(R.id.list_item_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]
        holder.itemText.text = item.name
        holder.itemIcon.setImageResource(R.drawable.profile_image)

        // Устанавливаем обработчик клика
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}