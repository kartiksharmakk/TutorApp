package com.example.myapplication.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Data.DataModel
import com.example.myapplication.Data.TestViewModel
import com.example.myapplication.databinding.CustomSelectGroupLayoutBinding
import com.facebook.shimmer.Shimmer
import com.google.firebase.storage.FirebaseStorage

class AdapterGroupTest(var context: Context, var list: List<DataModel.Group>, var viewModel: TestViewModel): RecyclerView.Adapter<AdapterGroupTest.TestViewHolder>() {
    val selectedStudentIds = mutableListOf<String>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterGroupTest.TestViewHolder {
        val binding = CustomSelectGroupLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  TestViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: AdapterGroupTest.TestViewHolder, position: Int) {
        val group = list[position]
        holder.bind(context, group, selectedStudentIds, viewModel)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class TestViewHolder(val binding: CustomSelectGroupLayoutBinding, val adapter: AdapterGroupTest): RecyclerView.ViewHolder(binding.root){
        fun bind(context: Context, group: DataModel.Group, selectedStudentIds: MutableList<String>, viewModel: TestViewModel){
            binding.apply {
                val shimmer = Shimmer.AlphaHighlightBuilder().setDuration(1000).setBaseAlpha(0.7f)
                    .setHighlightAlpha(0.6f).setDirection(Shimmer.Direction.LEFT_TO_RIGHT).setAutoStart(true).build()
                Log.d("AdapterGroupTest","Image: ${group.displayImage}")
                shimmerSelectStudentName.startShimmer()

                val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(group.displayImage)
                shimmerGroupImg.setShimmer(shimmer)
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(context)
                        .load(uri)
                        .into(selectGroupImage)
                }.addOnFailureListener { exception ->
                    Log.e("AdapterGroupTest", "Failed to get download URL: ${exception.message}")
                }

                txtSelectGroupName.setText(group.groupName)
                var isSelected = selectedStudentIds.all{selectedStudentIds.contains(it)}
                imgRadioButtonGroup.visibility = if (isSelected) View.VISIBLE else View.GONE

                cardRadioButtonGroup.setOnClickListener {
                    val students = group.students
                    if(isSelected){
                        selectedStudentIds.removeAll(students)
                        imgRadioButtonGroup.visibility = View.GONE
                        viewModel.removeSelectedGroupStudents(group.groupId)
                    }else{
                        selectedStudentIds.addAll(group.students)
                        imgRadioButtonGroup.visibility = View.VISIBLE
                        viewModel.addSelectedGroupStudents(group.groupId)
                    }
                    adapter.notifyItemChanged(adapterPosition)
                    isSelected = !isSelected
                }
            }
        }
    }
}