package com.example.teamup.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.R

class CheckboxListAdapter(
    private val items: List<String>,
    private val checkedStates: MutableList<Boolean>
) : RecyclerView.Adapter<CheckboxListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBox.text = items[position]
        holder.checkBox.isChecked = checkedStates[position]

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            checkedStates[position] = isChecked
        }
    }

    override fun getItemCount(): Int = items.size
}