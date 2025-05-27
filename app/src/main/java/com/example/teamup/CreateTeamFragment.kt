package com.example.teamup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Spinner
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class CreateTeamFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_team, container, false)

        val chooseEvent = view.findViewById<Spinner>(R.id.chooseEvent)
        val events = listOf("Hackathon", "Workshop", "Conference", "Meetup")
        val adapterEvents = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, events)
        chooseEvent.adapter = adapterEvents

        val editTextName = view.findViewById<EditText>(R.id.editTextName)
        val editTextDescription = view.findViewById<EditText>(R.id.editTextDescription)

        val tagInput = view.findViewById<AutoCompleteTextView>(R.id.tagInput)
        val chipGroup = view.findViewById<ChipGroup>(R.id.tagChipGroup)

        val predefinedTags = listOf("Android", "iOS", "Web Development", "Data Science", "Machine Learning", "AI", "Blockchain", "Cybersecurity", "Cloud Computing", "DevOps", "Game Development", "UI/UX Design", "AR/VR", "IoT", "Big Data", "Agile Methodologies", "Project Management")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, predefinedTags)
        tagInput.setAdapter(adapter)

        // Обработка ввода
        tagInput.setOnItemClickListener { parent, view, position, id ->
            val selectedTag = parent.getItemAtPosition(position) as String
            if (!isTagAlreadyAdded(chipGroup, selectedTag)) {
                addChipToGroup(chipGroup, selectedTag)
                tagInput.text.clear()
            }
        }

        return view
    }
    private fun isTagAlreadyAdded(chipGroup: ChipGroup, tag: String): Boolean {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.text.toString().equals(tag, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    private fun addChipToGroup(chipGroup: ChipGroup, tag: String) {
        val chip = Chip(requireContext()).apply {
            text = tag
            isCloseIconVisible = true
            setOnCloseIconClickListener { chipGroup.removeView(this) }
        }
        chipGroup.addView(chip)
    }
}