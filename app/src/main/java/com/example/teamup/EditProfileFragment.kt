package com.example.teamup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class EditProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        val nicknameEditText = view.findViewById<EditText>(R.id.editUserNickname)
        val nameEditText = view.findViewById<EditText>(R.id.editUserName)
        val emailEditText = view.findViewById<EditText>(R.id.editEmail)
        val contactsEditText = view.findViewById<EditText>(R.id.editContacts)
        val githubEditText = view.findViewById<EditText>(R.id.editGithub)
        val experienceEditText = view.findViewById<EditText>(R.id.editExperience)
        val editBio = view.findViewById<EditText>(R.id.editBio)

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