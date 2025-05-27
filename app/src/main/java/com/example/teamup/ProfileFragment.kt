package com.example.teamup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.Adapters.TeamAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ProfileFragment: Fragment(), TeamAdapter.OnItemClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val profileImage = view.findViewById<android.widget.ImageView>(R.id.profileImage)
        val userNickname = view.findViewById<TextView>(R.id.userNickname)
        val userName = view.findViewById<TextView>(R.id.userName)
        val emailText = view.findViewById<TextView>(R.id.emailText)
        val contactsText = view.findViewById<TextView>(R.id.contactsText)
        val githubText = view.findViewById<TextView>(R.id.githubText)
        val experienceText = view.findViewById<TextView>(R.id.experienceText)
        val bioText = view.findViewById<TextView>(R.id.bioText)
        val skillsChipGroup = view.findViewById<ChipGroup>(R.id.skillsChipGroup)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)


        profileImage.setImageResource(R.drawable.profile_image)
        userNickname.text = "ivanov_alex"
        userName.text = "Иванов Алексей Александрович"
        emailText.text  = "Email: ${"user@example.com"}"
        contactsText.text = "Контакты: ${"https://t.me/example"}"
        githubText.text = "GitHub: ${"https://github.com/example"}"
        experienceText.text = "1 год работы в Android-команде..."
        bioText.text = "Люблю писать код, изучаю Kotlin, участвую в хакатонах..."


        val skills = listOf("Kotlin", "Java", "Android", "SQL", "Leadership", "Time Management")
        skillsChipGroup.removeAllViews()
        for (skill in skills) {
            val chip = Chip(requireContext())
            chip.text = skill
            chip.isClickable = false
            chip.isCheckable = false
            skillsChipGroup.addView(chip)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = TeamAdapter(listOf(Team("SuperTeam", "Python Workshop", "Команда для участия в хакатоне по Python")), this@ProfileFragment)

        return view
    }
    override fun onItemClick(item: Team) {
        Toast.makeText(context, view.toString(), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_myTeamsFragment_to_teamFragment)
    }
}