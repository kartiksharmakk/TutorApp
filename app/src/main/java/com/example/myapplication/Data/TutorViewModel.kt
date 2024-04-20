package com.example.myapplication.Data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.launch

class TutorViewModel: ViewModel() {

    private val mutuableAbout = MutableLiveData<String>()


    val firebaseDatabase = Firebase.database
    val databaseReference = firebaseDatabase.getReference("User Details")
    val about: LiveData<String> get() = mutuableAbout

    fun updateAbout(context: Context,text: String){
        val email = Prefs.getUSerEmailEncoded(context)
        viewModelScope.launch {
            mutuableAbout.value = text
        }
        databaseReference.child(email!!).child("about").setValue(text)
    }

}