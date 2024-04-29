package com.example.myapplication.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestViewModel(var testRepository: TestRepository): ViewModel() {
    private val _test = MutableLiveData<DataModel.Test>()
    val test: LiveData<DataModel.Test> = _test

    private val _questions = MutableLiveData<MutableList<DataModel.Question>>()
    val questions: LiveData<MutableList<DataModel.Question>> = _questions

    fun addQuestionToTest(testId: String, question: DataModel.Question){
        testRepository.addQuestionToTest(testId, question)
    }
    fun saveTest(){
        var testToSave = test.value ?: return
        testToSave.questions = questions.value ?: mutableListOf()
        testRepository.saveTest(testToSave)
    }

    fun addEmptyQuestion(){
        val newQuestion = DataModel.Question("","" ,emptyList(),"",0)
        val currentQuestions = questions.value ?: mutableListOf()
        currentQuestions.add(newQuestion)
        _questions.postValue(currentQuestions)
    }
    fun initNewTest(creatorId: String){
        val newTest = DataModel.Test("", creatorId, emptyList(), mutableListOf())
        _test.value = newTest
    }
}