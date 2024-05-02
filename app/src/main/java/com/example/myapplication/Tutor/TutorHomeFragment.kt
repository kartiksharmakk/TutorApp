package com.example.myapplication.Tutor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.Data.Prefs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentTutorHomeBinding

class TutorHomeFragment : Fragment() {
    lateinit var binding: FragmentTutorHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorHomeBinding.inflate(inflater, container, false)
        binding.apply {
            val name = Prefs.getUsername(requireContext())
            txtUsernameTutorHome.setText(name)
        }
        return binding.root
    }

}