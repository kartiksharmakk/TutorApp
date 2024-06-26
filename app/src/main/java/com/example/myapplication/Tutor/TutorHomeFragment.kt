package com.example.myapplication.Tutor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.TutorGroupAdapter
import com.example.myapplication.Adapter.TutorTestAdapter
import com.example.myapplication.Data.DataModel
import com.example.myapplication.Data.Prefs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentTutorHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class TutorHomeFragment : Fragment() {
    lateinit var binding: FragmentTutorHomeBinding
    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var groupReference: DatabaseReference
    lateinit var testReference: DatabaseReference
    lateinit var groupAdapter: TutorGroupAdapter
    lateinit var testAdapter: TutorTestAdapter
    lateinit var groupList: MutableList<DataModel.Group>
    lateinit var testList: MutableList<DataModel.Test>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = Firebase.database
        groupReference = firebaseDatabase.getReference("Groups")
        testReference = firebaseDatabase.getReference("tests")
        val name = Prefs.getUsername(requireContext())
        groupList = mutableListOf()
        testList = mutableListOf()
        groupAdapter = TutorGroupAdapter(requireContext(), groupList){group ->
            val action = TutorHomeFragmentDirections.actionTutorHomeFragmentToViewGroupDetailsFragment(group.groupId)
            findNavController().navigate(action)
        }
        testAdapter = TutorTestAdapter(requireContext(), testList){test ->
            //Navigate with test is in bundles
        }
        binding.apply {
            txtUsernameTutorHome.setText(name)
            recyclerGroupTutorHome.adapter = groupAdapter
            recyclerTestsTutorHome.adapter = testAdapter
        }
        fetchGroups()
        fetchTests()
        return binding.root
    }
    private fun fetchGroups(){
        val uid = Prefs.getUID(requireContext())
        val query = groupReference.orderByChild("tutorId").equalTo(uid)
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList.clear()
                for (groupSnapshot in snapshot.children){
                    val group = groupSnapshot.getValue(DataModel.Group::class.java)
                    group?.let {
                        groupList.add(it)
                    }
                }
                Prefs.saveGroupCount(requireContext(),groupList.size)
                groupAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun fetchTests(){
        val uid = Prefs.getUID(requireContext())
        val query = testReference.orderByChild("creatorId").equalTo(uid)
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                testList.clear()
                for(testSnapshot in snapshot.children){
                    val test = testSnapshot.getValue(DataModel.Test::class.java)
                    test?.let {
                        testList.add(it)
                    }
                }
                Prefs.saveTestCount(requireContext(), testList.size)
                testAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}