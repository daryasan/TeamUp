package com.example.teamup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class EventFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        val eventImageView : ImageView = view.findViewById<ImageView>(R.id.eventImage)
        eventImageView.setImageResource(R.drawable.fish_image)

        val eventDescription = view.findViewById<TextView>(R.id.eventDescription)
        eventDescription.setText("big text big text big text big text big text big text big text big text big text big text big text big text")

        return view
    }
}