package com.example.myapplication.Tutor

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.QuestionAdapter
import com.example.myapplication.Data.DataModel
import com.example.myapplication.Data.Prefs
import com.example.myapplication.Data.TestRepository
import com.example.myapplication.Data.TestViewModel
import com.example.myapplication.Data.TestViewModelFactory
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
    lateinit var adapter: QuestionAdapter

    val firebaseDatabase = Firebase.database
    val testRepository = TestRepository(firebaseDatabase)
    val viewModelFactory = TestViewModelFactory(testRepository)
    val viewModel: TestViewModel by viewModels { viewModelFactory }
    var testId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTestBinding.inflate(inflater, container, false)
        viewModel.testRepository = testRepository

        val uid = Prefs.getUID(requireContext())
        viewModel.initNewTest(uid!!)
        viewModel.addEmptyQuestion()
        adapter = QuestionAdapter(viewModel.questions.value?: mutableListOf(), this)

        binding.rvTest.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTest.adapter = adapter
        viewModel.questions.observe(viewLifecycleOwner){questions->
            adapter.questions = questions
            adapter.notifyDataSetChanged()
        }
        binding.imgBackCreateTest.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgSaveCreateTest.setOnClickListener {
           testId = testRepository.saveTest(viewModel.test.value!!)
            viewModel.questions.value?.forEach { question ->
                viewModel.addQuestionToTest(testId, question)
            }

        }
        binding.imgAddQuestion.setOnClickListener {
            viewModel.addEmptyQuestion()
            adapter.notifyItemInserted(adapter.itemCount)
        }



        return binding.root
    }

    private fun onAddQuestionClicked(){
        viewModel.addEmptyQuestion()
        adapter.notifyItemInserted(adapter.itemCount)
    }

    override fun onQuestionInteraction(question: DataModel.Question, position: Int) {
        viewModel.addQuestionToTest(testId, question)
    }
}