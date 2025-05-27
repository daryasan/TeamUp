package com.example.teamup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnDeleteAccount = view.findViewById<Button>(R.id.btnDeleteAccount)

        btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment)
        }
        btnLogout.setOnClickListener {
            //findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
        }
        btnDeleteAccount.setOnClickListener {
            // Implement account deletion logic here
        }
        return view
    }
}