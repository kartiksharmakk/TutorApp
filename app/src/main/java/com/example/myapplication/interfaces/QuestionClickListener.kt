package com.example.myapplication.interfaces

import com.example.myapplication.Adapter.QuestionAdapter
import com.example.myapplication.Data.DataModel

interface QuestionClickListener {
    fun onQuestionInteraction(question: DataModel.Question, position: Int)
}