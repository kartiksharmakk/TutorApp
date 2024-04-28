package com.example.myapplication.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TestRepository(val database: FirebaseDatabase) {
    private val testRef = database.getReference("tests")

    fun saveTest(test: DataModel.Test){
        val testId = "test" + testRef.push().key!!
        testRef.child(testId).setValue(test)

        test.questions.forEach { question ->
            saveQuestion(question, testId)
        }
    }

    private fun saveQuestion(question: DataModel.Question, testId: String){
        val questionRef = testRef.child(testId).child("questions")
        questionRef.child(question.questionId).setValue(question)
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