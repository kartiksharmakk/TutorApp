package com.example.myapplication.Tutor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCreateGroupBinding

class CreateGroupFragment : Fragment() {
    lateinit var bindinng: FragmentCreateGroupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindinng = FragmentCreateGroupBinding.inflate(inflater, container, false)



        return bindinng.root
    }
}