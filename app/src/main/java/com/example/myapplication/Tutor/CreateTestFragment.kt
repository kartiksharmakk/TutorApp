package com.example.myapplication.Tutor

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Adapter.QuestionAdapter
import com.example.myapplication.Data.Prefs
import com.example.myapplication.Data.TestRepository
import com.example.myapplication.Data.TestViewModel
import com.example.myapplication.Data.TutorViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCreateTestBinding
import com.example.myapplication.interfaces.QuestionClickListener
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class CreateTestFragment : Fragment(), QuestionClickListener {
    lateinit var binding: FragmentCreateTestBinding
    lateinit var viewModel: TestViewModel
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var adapter: QuestionAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = requireActivity()
        viewModel = ViewModelProvider(activity).get(TestViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTestBinding.inflate(inflater, container, false)
        firebaseDatabase = Firebase.database
        val testRepository = TestRepository(firebaseDatabase)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(TestViewModel::class.java)
        viewModel.testRepository = testRepository

        val uid = Prefs.getUID(requireContext())
        viewModel.initNewTest(uid!!)

        adapter = QuestionAdapter(viewModel.questions.value?: mutableListOf(),  {onAddQuestionClicked()}, this)
        binding.rvTest.adapter = adapter
        viewModel.questions.observe(viewLifecycleOwner){questions->


        }
        binding.imgBackCreateTest.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgSaveCreateTest.setOnClickListener {
            viewModel.saveTest()
        }
        binding.imgAddQuestion.setOnClickListener {
            viewModel.addEmptyQuestion()
        }



        return binding.root
    }

    private fun onAddQuestionClicked(){
        viewModel.addEmptyQuestion()
        adapter.notifyItemInserted(adapter.itemCount)
    }

    override fun onQuestionInteraction(question: QuestionAdapter.QuestionItem, position: Int) {
        TODO("Not yet implemented")
    }
}