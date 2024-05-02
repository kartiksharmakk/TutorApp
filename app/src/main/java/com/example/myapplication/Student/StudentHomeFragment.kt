package com.example.myapplication.Student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.Data.Prefs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStudentHomeBinding

class StudentHomeFragment : Fragment() {
    lateinit var binding: FragmentStudentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            val name = Prefs.getUsername(requireContext())
            txtUsernameStudentHome.setText(name)
        }

        return binding.root
    }
}