package com.example.teamup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamup.Adapters.EventCardsAdapter
import com.example.teamup.Adapters.TeamAdapter

class MyEventsFragment : Fragment(), EventCardsAdapter.OnItemClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_events, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        var cards = listOf(
            CardEvent("Event 1", "bla bla bla", "2023-10-01", 0),
            CardEvent("Card 2", "bebebe", "2023-10-02", 1),
            CardEvent("Card 3", "aaaaa", "2023-10-03", 2),
        )
        recyclerView.adapter = EventCardsAdapter(cards, this)

        val button = view.findViewById<Button>(R.id.buttonNewEvent)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_myEventsFragment_to_manageEventFragment)
        }
        return view
    }
    override fun onItemClick(item: CardEvent) {
        Toast.makeText(context, view.toString(), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_myEventsFragment_to_manageEventFragment)
    }
}