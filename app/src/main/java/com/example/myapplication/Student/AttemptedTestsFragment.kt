package com.example.myapplication.Student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAttemptedTestsBinding

class AttemptedTestsFragment : Fragment() {
    lateinit var binding: FragmentAttemptedTestsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttemptedTestsBinding.inflate(inflater, container, false)
        return binding.root
    }
}