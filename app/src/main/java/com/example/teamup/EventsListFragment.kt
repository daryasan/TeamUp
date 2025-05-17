package com.example.teamup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EventsListFragment : Fragment(), EventCardsAdapter.OnItemClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events_list, container, false)

        val button = view.findViewById<Button>(R.id.sorting_button)
        button.setOnClickListener { v: View ->
            showMenu(v, R.menu.sorting_popup_menu)
        }
        val filterButton = view.findViewById<Button>(R.id.filter_button)
        filterButton.setOnClickListener {
            val filterSideSheet = FilterSideSheetFragment()
            filterSideSheet.show(parentFragmentManager, "FilterSideSheet")
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val cards = listOf(
            CardEvent("Event 1", "bla bla bla", "2023-10-01", 0),
            CardEvent("Card 2", "bebebe", "2023-10-02", 1),
            CardEvent("Card 3", "aaaaa", "2023-10-03", 2),
        )

        /*val adapter = EventCardsAdapter(cards) { card ->
            Toast.makeText(context, "Clicked: ${card.title}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_eventsListFragment_to_eventFragment)
        }*/
        recyclerView.adapter = EventCardsAdapter(cards, this)


        return view
    }

    override fun onItemClick(item: CardEvent) {
        //Toast.makeText(context, "Clicked: ${item.title}", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, view.toString(), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_eventsListFragment_to_eventFragment)
    }
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
        when (menuItem.itemId) {
                R.id.option_1 -> {
                    // Handle sort by date action
                    Toast.makeText(requireContext(), "Sort by relevance selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.option_2 -> {
                    // Handle sort by name action
                    Toast.makeText(requireContext(), "Sort by date selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.option_3 -> {
                    // Handle sort by name action
                    Toast.makeText(requireContext(), "Sort by newness selected", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }
}