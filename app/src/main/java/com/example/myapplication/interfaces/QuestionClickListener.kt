package com.example.myapplication.interfaces

import com.example.myapplication.Adapter.QuestionAdapter
import com.example.myapplication.Data.DataModel

interface QuestionClickListener {
    fun onQuestionInteraction(question: DataModel.Question, position: Int)
    fun onSaveClicked(question: String, option1: String, option2: String, option3: String, option4: String, marks: String, answer: String)
}