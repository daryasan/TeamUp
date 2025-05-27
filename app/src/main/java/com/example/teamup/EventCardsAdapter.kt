package com.example.teamup
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class EventCardsAdapter(
    private val items: List<CardEvent>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<EventCardsAdapter.CardViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: CardEvent)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.eventTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.eventDescription)
        val dateTextView: TextView = itemView.findViewById(R.id.eventDate)
        val eventImageView: ImageView = itemView.findViewById(R.id.eventImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]
        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description
        holder.dateTextView.text = item.date
        holder.eventImageView.setImageResource(R.drawable.fish_image)

        // Устанавливаем обработчик клика
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}