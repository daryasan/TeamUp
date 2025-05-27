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
import com.example.teamup.Adapters.PeopleAdapter

class TeamFragment: Fragment(), PeopleAdapter.OnItemClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_team, container, false)
        val teamName = view.findViewById<TextView>(R.id.teamName)
        teamName.text = "SuperTeam"

        val teamStatus = view.findViewById<TextView>(R.id.teamStatus)
        teamStatus.text = "Собрана"

        val teamEvent = view.findViewById<TextView>(R.id.teamEvent)
        teamEvent.text = "Python Workshop"

        val teamDescription = view.findViewById<TextView>(R.id.teamDescription)
        teamDescription.text = "Команда для участия в хакатоне по Python"

        val recyclerViewMembers = view.findViewById<RecyclerView>(R.id.recyclerViewMembers)
        recyclerViewMembers.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMembers.adapter = PeopleAdapter(listOf(Person("Anton"), Person("Boris"), Person("Ivan")), this)

        val recyclerViewMentor = view.findViewById<RecyclerView>(R.id.recyclerViewMentor)
        recyclerViewMentor.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMentor.adapter = PeopleAdapter(listOf(Person("Alexey")), this)



        return view
    }
    override fun onItemClick(item: Person) {
        Toast.makeText(context, view.toString(), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_teamFragment_to_profileFragment)
    }
}