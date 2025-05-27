package com.example.teamup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.Adapters.MeetingAdapter
import com.example.teamup.Adapters.PeopleAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ManageTeamFragment : Fragment(), PeopleAdapter.OnItemClickListener{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_team, container, false)

        val teamName = view.findViewById<TextView>(R.id.teamName)
        val teamDescription = view.findViewById<TextView>(R.id.teamDescription)
        val teamStatus = view.findViewById<TextView>(R.id.teamStatus)
        val teamEvent = view.findViewById<TextView>(R.id.teamEvent)

        teamName.text = "SuperTeam"
        teamDescription.text = "Команда для участия в хакатоне по Python"
        teamStatus.text = "Собрана"
        teamEvent.text = "Python Workshop"

        val skillsChipGroup = view.findViewById<ChipGroup>(R.id.skillsChipGroup)
        val skills = listOf("Kotlin", "Java", "Android", "SQL", "Leadership", "Time Management")
        skillsChipGroup.removeAllViews()
        for (skill in skills) {
            val chip = Chip(requireContext())
            chip.text = skill
            chip.isClickable = false
            chip.isCheckable = false
            skillsChipGroup.addView(chip)
        }

        val recyclerViewMembers = view.findViewById<RecyclerView>(R.id.recyclerViewMembers)
        recyclerViewMembers.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMembers.adapter = PeopleAdapter(listOf(Person("Anton"), Person("Boris"), Person("Ivan")), this)

        val recyclerViewMentor = view.findViewById<RecyclerView>(R.id.recyclerViewMentor)
        recyclerViewMentor.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMentor.adapter = PeopleAdapter(listOf(Person("Alexey")), this)

        val recyclerViewMeetings = view.findViewById<RecyclerView>(R.id.recyclerViewMeetings)
        recyclerViewMeetings.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMeetings.adapter = MeetingAdapter(
            listOf(
                CardEvent("Weekly Sync", "https://example.com/meeting1", "2023-10-01 16:00", 0),
                CardEvent("Brain Storm", "https://example.com/meeting2", "2023-10-05 10:30", 1)
            )
        )

        val buttonNewMeeting = view.findViewById<TextView>(R.id.buttonNewMeeting)
        buttonNewMeeting.setOnClickListener {
            findNavController().navigate(R.id.action_manageTeamFragment_to_createMeetingFragment)
        }


        return view
    }
    override fun onItemClick(item: Person) {
        Toast.makeText(context, view.toString(), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_manageTeamFragment_to_profileFragment)
    }
}