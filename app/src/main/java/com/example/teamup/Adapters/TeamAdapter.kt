package com.example.teamup.Adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.Person
import com.example.teamup.R
import com.example.teamup.Team

class TeamAdapter (
    private val items: List<Team>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TeamAdapter.CardViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Team)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText1: TextView = itemView.findViewById(R.id.list_item_text)
        val itemText2: TextView = itemView.findViewById(R.id.list_item_second_text)
        val itemText3: TextView = itemView.findViewById(R.id.list_item_third_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]
        holder.itemText1.text = item.name
        holder.itemText2.text = item.event
        holder.itemText3.text = item.description

        // Устанавливаем обработчик клика
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}