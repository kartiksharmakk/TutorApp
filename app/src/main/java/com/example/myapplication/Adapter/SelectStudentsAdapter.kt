package com.example.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Data.DataModel
import com.example.myapplication.databinding.CustomSelectStudentBinding

class SelectStudentsAdapter(var context: Context,var list: ArrayList<DataModel.Students>): RecyclerView.Adapter<SelectStudentsAdapter.StudentViewHolder>() {

    val selectedStudentIds = mutableListOf<String>()
    class StudentViewHolder(val binding: CustomSelectStudentBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(context: Context, student: DataModel.Students, selectedStudentIds: MutableList<String>){
            binding.apply {
                Glide.with(context).load(student.image).into(binding.imgSelectStudent)
                binding.txtSelectStudentName.setText(student.name)
                binding.txtSelectStudentPhone.setText(student.phone)

                val isSelected = selectedStudentIds.contains(student.studentId)
                binding.imgRadioButton.visibility = if (isSelected) View.VISIBLE else View.GONE


                binding.cardRadioButton.setOnClickListener {
                    if(isSelected){
                        selectedStudentIds.remove(student.image)
                        binding.imgRadioButton.visibility = View.GONE
                    }else{
                        selectedStudentIds.add(student.studentId)
                        binding.imgRadioButton.visibility = View.VISIBLE
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = CustomSelectStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = list[position]
        holder.bind(context,student, selectedStudentIds)
    }

    fun getSelectStudentIds(): List<String>{
        return selectedStudentIds
    }
}