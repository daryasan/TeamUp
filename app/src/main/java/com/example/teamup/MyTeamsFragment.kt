package com.example.teamup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.Adapters.TeamAdapter

class MyTeamsFragment: Fragment(), TeamAdapter.OnItemClickListener{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_teams, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = TeamAdapter(listOf(
            Team("SuperTeam", "Python Workshop", "Description")
        ), this@MyTeamsFragment)

        val button = view.findViewById<View>(R.id.buttonNewTeam)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_myTeamsFragment_to_createTeamFragment)
        }


        return view
    }
    override fun onItemClick(item: Team) {
        Toast.makeText(context, view.toString(), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_myTeamsFragment_to_manageTeamFragment)
    }
}