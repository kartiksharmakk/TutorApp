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
import com.example.myapplication.databinding.CustomSelectStudentBinding
import com.facebook.shimmer.Shimmer
import com.google.firebase.storage.FirebaseStorage

class AdapterStudentTest(var context: Context, var list: List<DataModel.Students>, val viewModel: TestViewModel): RecyclerView.Adapter<AdapterStudentTest.TestViewHolder>() {
    val selectedStudentIds = mutableListOf<String>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterStudentTest.TestViewHolder {
        val binding = CustomSelectStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: AdapterStudentTest.TestViewHolder, position: Int) {
        val student = list[position]
        holder.bind(context, student, selectedStudentIds, viewModel)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class TestViewHolder(val binding: CustomSelectStudentBinding, val adapter: AdapterStudentTest): RecyclerView.ViewHolder(binding.root){
        fun bind(context: Context, student: DataModel.Students, selectedStudentIds: MutableList<String>, viewModel: TestViewModel){
            binding.apply {
                val shimmer = Shimmer.AlphaHighlightBuilder().setDuration(1000).setBaseAlpha(0.7f)
                    .setHighlightAlpha(0.6f).setDirection(Shimmer.Direction.LEFT_TO_RIGHT).setAutoStart(true).build()
                Log.d("AdapterStudentTest","Image: ${student.image}")
                shimmerSelectStudentName.startShimmer()
                shimmerSelectStudentPhone.startShimmer()

                val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(student.image)
                shimmerSelectStudentImage.setShimmer(shimmer)
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(context)
                        .load(uri)
                        .into(imgSelectStudent)
                }.addOnFailureListener { exception ->
                    Log.e("Select Student", "Failed to get download URL: ${exception.message}")
                }

                txtSelectStudentName.setText(student.name)
                txtSelectStudentPhone.setText(student.phone)

                var isSelected = selectedStudentIds.contains(student.studentId)
                imgRadioButton.visibility = if (isSelected) View.VISIBLE else View.GONE

                cardRadioButton.setOnClickListener {
                    val studentId = student.studentId
                    val st = DataModel.TestAssignedTo(studentId, false)
                    if(isSelected){
                        selectedStudentIds.remove(student.studentId)
                        imgRadioButton.visibility = View.GONE
                        viewModel.removeSelectedStudent(st)
                    }else{
                        selectedStudentIds.add(student.studentId)
                        imgRadioButton.visibility = View.VISIBLE
                        viewModel.addSelectedStudent(st)
                    }
                    adapter.notifyItemChanged(adapterPosition)
                    isSelected = !isSelected
                }
            }

        }
    }
}