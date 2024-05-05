package com.example.myapplication.Student

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.Adapter.AdapterPendingTests
import com.example.myapplication.Data.DataModel
import com.example.myapplication.Data.Prefs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAttemptedTestsBinding
import com.example.myapplication.databinding.FragmentPendingTestsBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class PendingTestsFragment : Fragment() {

    lateinit var binding: FragmentPendingTestsBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var testReference: DatabaseReference
    lateinit var pendingAdapter: AdapterPendingTests
    lateinit var pendingTests: MutableList<DataModel.Test>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendingTestsBinding.inflate(inflater, container, false)
        firebaseDatabase = Firebase.database
        testReference = firebaseDatabase.getReference("tests")
        pendingTests = mutableListOf()
        pendingAdapter = AdapterPendingTests(requireContext(), pendingTests){tests->
            //Pending
        }
        binding.rvPendingTests.adapter = pendingAdapter
        retrieveTests()
        return binding.root
    }

    fun retrieveTests(){
        val uid = Prefs.getUID(requireContext())
        testReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(testSnapshot in snapshot.children){
                    val test = testSnapshot.getValue(DataModel.Test::class.java)
                    test?.let {
                        Log.d("PendingTests", "Processing test: $it")
                        val assignedTo = it.assignedTo.find { assignedTo -> assignedTo.studentId == uid }
                        if(assignedTo != null){
                            if(!assignedTo.hasAttempted){
                                pendingTests.add(it)
                            }
                        }
                    }
                }
                Log.d("PendingTests","List : $pendingTests")
                pendingAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PendingTests","Error : $error")
            }

        })

    }
}