package com.example.myapplication.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TestRepository(val database: FirebaseDatabase) {
    private val testRef = database.getReference("tests")

    fun generateTestId(): String{
        return "test${testRef.push().key!!}"
    }
    fun generateQuestionId(testId: String): String{
        return  testRef.child(testId).child("questions").push().key!!
    }
    fun saveTestAndQuestions(testId: String, questions: List<DataModel.Question>){
        testRef.child(testId).setValue(DataModel.Test(testId,"", emptyList(), emptyList()))//Add data

        questions.forEach { question ->
            val questionId = generateQuestionId(testId)
            question.questionId = questionId
            saveQuestion(question, testId)
        }
        Log.d("TestRepository","Test saved to DB")
    }

    private fun saveQuestion(question: DataModel.Question, testId: String){
        val questionRef = testRef.child(testId).child("questions")
        questionRef.child(question.questionId).setValue(question)
        Log.d("TestRepository","Question saved to DB")
    }
    fun addQuestionToTest(testId: String, question: DataModel.Question){
        val questionId = testRef.child("$testId/questions").push().key
        question.questionId = questionId!!
        testRef.child("$testId/questions").child(questionId!!).setValue(question)
    }
    fun getTestByStudentId(studentId: String): LiveData<List<DataModel.Test>>{
        val liveDataTests = MutableLiveData<List<DataModel.Test>>()
        val valueEventListener = testRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val testList = mutableListOf<DataModel.Test>()
                    for(child in snapshot.children){
                        val test = child.getValue(DataModel.Test::class.java)
                        if(test != null && test.assignedTo.contains(studentId)){
                            testList.add(test)
                        }
                    }
                    liveDataTests.postValue(testList.toList())
                }else{
                    liveDataTests.postValue(emptyList())
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return liveDataTests
    }

    fun getTestByTutorId(tutorId: String): LiveData<List<DataModel.Test>>{
        val liveDataTests = MutableLiveData<List<DataModel.Test>>()
        val valueEventListener = testRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val testList = mutableListOf<DataModel.Test>()
                    for (child in snapshot.children) {
                        val test = child.getValue(DataModel.Test::class.java)
                        if (test != null && test.creatorId == tutorId) {
                            testList.add(test)
                        }
                    }
                    liveDataTests.postValue(testList.toList())
                } else {
                    liveDataTests.postValue(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                liveDataTests.postValue(emptyList())
            }

        })

        return liveDataTests
    }

    fun getTestByTestId(testId: String): LiveData<DataModel.Test?>{
        val testRef = testRef.child(testId)
        val liveDataTest = MutableLiveData<DataModel.Test?>()
        val valueEventListener = testRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val test = snapshot.getValue(DataModel.Test::class.java)
                    liveDataTest.postValue(test)
                }else{
                    liveDataTest.postValue(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                liveDataTest.postValue(null)
            }
        })
        return  liveDataTest
    }

    fun getAllTests(): LiveData<List<DataModel.Test>>{
        val liveDataTests = MutableLiveData<List<DataModel.Test>>()
        val valueEventListener = testRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val testList = mutableListOf<DataModel.Test>()
                    for(child in snapshot.children){
                        val test = child.getValue(DataModel.Test::class.java)
                        if(test != null){
                            testList.add(test)
                        }
                    }
                    liveDataTests.postValue(testList.toList())
                }else{
                    liveDataTests.postValue(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                liveDataTests.postValue(emptyList())
            }
        })
        return  liveDataTests
    }
}