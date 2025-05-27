package com.example.teamup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.Adapters.StepListAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ManageEventFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manage_event, container, false)

        val eventImage = view.findViewById<ImageView>(R.id.eventImage)
        val changePhotoButton = view.findViewById<Button>(R.id.changePhotoButton)
        val editTextEventName = view.findViewById<EditText>(R.id.editTextEventName)
        val editTextEventDescription = view.findViewById<EditText>(R.id.editTextEventDescription)
        val editTextEventLink = view.findViewById<EditText>(R.id.editTextEventLink)
        val buttonNewStep = view.findViewById<Button>(R.id.buttonNewStep)

        eventImage.setImageResource(R.drawable.abstract_background)
        editTextEventName.setText("Hackathon 2023")
        editTextEventDescription.setText("Join us for an exciting hackathon where teams will compete to build innovative solutions in just 48 hours!")
        editTextEventLink.setText("https://example.com/hackathon2023")


        val tagInput = view.findViewById<AutoCompleteTextView>(R.id.tagInput)
        val chipGroup = view.findViewById<ChipGroup>(R.id.tagChipGroup)

        val predefinedTags = listOf("Android", "Project Management")
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

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewSteps)
        recyclerView.layoutManager = LinearLayoutManager(context)

        var cards = listOf(
            CardEvent("Step 1", "bla bla bla", "2023-10-01", 0),
            CardEvent("Step 2", "bebebe", "2023-10-02", 1),
            CardEvent("Step 3", "aaaaa", "2023-10-03", 2),
        )

        recyclerView.adapter = StepListAdapter(cards)

        buttonNewStep.setOnClickListener {
            findNavController().navigate(R.id.action_manageEventFragment_to_createStepFragment)
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