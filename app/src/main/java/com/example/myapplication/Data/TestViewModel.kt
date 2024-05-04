package com.example.myapplication.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class
TestViewModel(var testRepository: TestRepository): ViewModel() {
    private val _test = MutableLiveData<DataModel.Test>()
    val test: LiveData<DataModel.Test> = _test

    private val _questions = MutableLiveData<MutableList<DataModel.Question>>()
    val questions: LiveData<MutableList<DataModel.Question>> = _questions

    private val _allotedTo = MutableLiveData<MutableList<DataModel.TestAssignedTo>>()
    val allotedTo: LiveData<MutableList<DataModel.TestAssignedTo>> = _allotedTo


    fun addQuestion(question: DataModel.Question){
        val currentQuestions = _questions.value ?: mutableListOf()
        currentQuestions.add(question)
        _questions.value = currentQuestions
    }
    fun addQuestionToTest(testId: String, question: DataModel.Question){
        testRepository.addQuestionToTest(testId, question)
    }

    fun saveTestAndQuestions(testId: String,questionsList:ArrayList<DataModel.Question>,uid: String?,studentIdsList: ArrayList<String>){
        //val testId = testRepository.generateTestId()
        /*
        val questionsWithIds = _questions.value?.map{question ->
            question.copy(questionId = testRepository.generateQuestionId(testId))
        }?: emptyList()

         */
        testRepository.saveTestAndQuestions(testId, questionsList,uid,studentIdsList)
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

    fun addSelectedStudent(student: DataModel.TestAssignedTo){
       val selectedStudents = _allotedTo.value?.toMutableSet() ?: mutableListOf()
       selectedStudents.add(student.copy(hasAttempted = false))
        _allotedTo.value = selectedStudents.toMutableList()
    }

    fun removeSelectedStudent(student: DataModel.TestAssignedTo){
        val selectedStudents = _allotedTo.value?.toMutableList() ?: mutableListOf()
        selectedStudents.remove(student)
        _allotedTo.value = selectedStudents

    }

    fun getStudentByGroup(groupId: String): List<String>?{
        return testRepository.getStudentByGroup(groupId)
    }
    fun addSelectedGroupStudents(groupId: String){
        val studentsInGroup = getStudentByGroup(groupId)
        studentsInGroup?.let {
            val selectedStudents = _allotedTo.value?.toMutableList() ?: mutableListOf()
            it.forEach { studentId ->
                selectedStudents.add(DataModel.TestAssignedTo(studentId,false))
            }
            _allotedTo.value = selectedStudents
        }
    }
    fun removeSelectedGroupStudents(groupId: String){
        val studentsInGroup = getStudentByGroup(groupId)
        studentsInGroup?.let{
            val selectedStudents = _allotedTo.value?.toMutableList() ?: mutableListOf()
            it.forEach { studentId ->
                selectedStudents.retainAll{it.studentId == studentId}
            }
            _allotedTo.value = selectedStudents
        }
    }
}