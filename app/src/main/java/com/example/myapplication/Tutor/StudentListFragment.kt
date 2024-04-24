package com.example.myapplication.Tutor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.SelectStudentsAdapter
import com.example.myapplication.Data.DataModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStudentListBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class StudentListFragment : Fragment() {
    lateinit var binding: FragmentStudentListBinding
    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    private lateinit var adapter: SelectStudentsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentListBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = Firebase.database
        databaseReference = firebaseDatabase.getReference("Student")
        showRecyclerView()
        loadStudents()
        binding.imgBackAddStudents.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgSubmit.setOnClickListener {

        }

        return binding.root
    }

    fun showRecyclerView(){
        adapter = SelectStudentsAdapter(requireContext(), ArrayList())
        binding.recyclerAddStudent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@StudentListFragment.adapter
        }
    }

    private fun loadStudents() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val students = ArrayList<DataModel.Students>()
                for (studentSnapshot in snapshot.children) {
                    val student = studentSnapshot.getValue(DataModel.Students::class.java)
                    student?.let {
                        students.add(student)
                    }
                }
                adapter.list = students
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}