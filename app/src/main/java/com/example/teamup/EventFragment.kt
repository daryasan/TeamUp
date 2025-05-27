package com.example.teamup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.Adapters.StepListAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class EventFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        val eventImageView : ImageView = view.findViewById<ImageView>(R.id.eventImage)
        eventImageView.setImageResource(R.drawable.abstract_background)

        val eventTitle = view.findViewById<TextView>(R.id.eventTitle)
        eventTitle.setText("Python Workshop")

        val eventDescription = view.findViewById<TextView>(R.id.eventDescription)
        eventDescription.setText("Learn the basics of Python programming in this hands-on workshop.")

        val eventLinks = view.findViewById<TextView>(R.id.eventLinks)
        eventLinks.setText("https://example.com/workshop")

        val skillsChipGroup = view.findViewById<ChipGroup>(R.id.skillsChipGroup)
        val skills = listOf("Python", "Programming", "Workshop", "Hands-on", "Basics")
        skillsChipGroup.removeAllViews()
        for (skill in skills) {
            val chip = Chip(requireContext())
            chip.text = skill
            chip.isClickable = false
            chip.isCheckable = false
            skillsChipGroup.addView(chip)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewSteps)
        recyclerView.layoutManager = LinearLayoutManager(context)

        var cards = listOf(
            CardEvent("Этап 1: Регистрация", "Регистрация", "2025-10-01", 0),
            CardEvent("Этап 2: Соревнование", "Соревнование", "2023-10-02", 1),
            CardEvent("Этап 3: Награждение", "Награждение", "2023-10-03", 2),
        )

        recyclerView.adapter = StepListAdapter(cards)

        return view
    }
}