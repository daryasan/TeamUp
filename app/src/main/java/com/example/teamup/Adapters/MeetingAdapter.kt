package com.example.teamup.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.CardEvent
import com.example.teamup.R

class MeetingAdapter(
    private val steps: List<CardEvent>,
    //private val listener: OnStepClickListener
) : RecyclerView.Adapter<MeetingAdapter.StepViewHolder>() {

    /*interface OnStepClickListener {
        fun onStepClick(step: CardEvent)
    }*/

    class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val meetingName: TextView = itemView.findViewById(R.id.meetingName)
        val meetingDate: TextView = itemView.findViewById(R.id.meetingDate)
        val meetingLink : TextView = itemView.findViewById(R.id.meetingLink)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meeting, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = steps[position]
        holder.meetingName.text = step.title
        holder.meetingDate.text = step.date
        holder.meetingLink.text = step.description

        /*holder.itemView.setOnClickListener {
            listener.onStepClick(step)
        }*/
    }

    override fun getItemCount(): Int {
        return steps.size
    }
}