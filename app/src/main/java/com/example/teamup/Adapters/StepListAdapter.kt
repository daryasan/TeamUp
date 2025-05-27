package com.example.teamup.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.CardEvent
import com.example.teamup.R

class StepListAdapter(
    private val steps: List<CardEvent>,
    //private val listener: OnStepClickListener
) : RecyclerView.Adapter<StepListAdapter.StepViewHolder>() {

    interface OnStepClickListener {
        fun onStepClick(step: CardEvent)
    }

    class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stepName: TextView = itemView.findViewById(R.id.stepName)
        val stepDate: TextView = itemView.findViewById(R.id.stepDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_step, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = steps[position]
        holder.stepName.text = step.title
        holder.stepDate.text = step.date

        /*holder.itemView.setOnClickListener {
            listener.onStepClick(step)
        }*/
    }

    override fun getItemCount(): Int {
        return steps.size
    }
}