package com.example.teamup

import android.os.Bundle
import android.provider.Contacts.People
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.Adapters.EventCardsAdapter
import com.example.teamup.Adapters.PeopleAdapter
import com.example.teamup.Adapters.TeamAdapter
import com.google.android.material.tabs.TabLayout

class FindingPeopleFragment : Fragment(), PeopleAdapter.OnItemClickListener, TeamAdapter.OnItemClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_finding_people, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)

        recyclerView.adapter = PeopleAdapter(listOf(Person("Anton"), Person("Boris"), Person("Ivan"), Person("Sasha")), this@FindingPeopleFragment)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        recyclerView.adapter = PeopleAdapter(listOf(Person("Anton"), Person("Boris"), Person("Ivan"), Person("Sasha")), this@FindingPeopleFragment)
                    }
                    1 -> {
                        recyclerView.adapter = PeopleAdapter(listOf(Person("Anton"), Person("Boris"), Person("Ivan"), Person("Sasha")), this@FindingPeopleFragment)
                    }
                    2 -> {
                        recyclerView.adapter = TeamAdapter(listOf(Team("SuperTeam", "Event 1", "Description"), Team("Mega Team", "Event 2", "Description"), Team("Lonely Sasha", "Event 1", "Description")), this@FindingPeopleFragment)
                    }
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
        tabLayout.getTabAt(0)?.select() // Select the first tab by default

        return view
    }
    override fun onItemClick(item: Person) {
        Toast.makeText(context, view.toString(), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_findingPeopleFragment_to_profileFragment)
    }

    override fun onItemClick(item: Team) {
        Toast.makeText(context, view.toString(), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_findingPeopleFragment_to_teamFragment)
    }
}