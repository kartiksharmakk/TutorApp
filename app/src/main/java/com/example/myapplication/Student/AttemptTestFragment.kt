package com.example.myapplication.Student

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.AttemptTestAdapter
import com.example.myapplication.Data.DataModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAttemptTestBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class AttemptTestFragment : Fragment() {
    lateinit var binding: FragmentAttemptTestBinding
    lateinit var adapter: AttemptTestAdapter
    val questionsList = mutableListOf<DataModel.Question>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttemptTestBinding.inflate(inflater, container, false)
        val testId = AttemptTestFragmentArgs.fromBundle(requireArguments()).testId
        adapter = AttemptTestAdapter(requireContext(), questionsList)
        binding.apply {
            imgBackAttemptTest.setOnClickListener{
                findNavController().popBackStack()
            }
            rvAttemptTest.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = this@AttemptTestFragment.adapter
            }
        }
        fetchQuestions(testId)
        return binding.root
    }

    private fun fetchQuestions(testId: String) {
        val testsReference = Firebase.database.getReference("tests").child(testId)
        testsReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val test = snapshot.getValue(DataModel.Test::class.java)
                    questionsList.clear()
                    test?.let{
                        binding.txtTestNameSt.text = it.testName
                        val questions = it.questions
                        questionsList.addAll(questions)
                    }
                    adapter.notifyDataSetChanged()
                }else{
                    Log.e("AttemptTestFragment", "Test with ID $testId does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AttemptTestFragment", "Error fetching test data: ${error.message}")
            }

        })
    }
}