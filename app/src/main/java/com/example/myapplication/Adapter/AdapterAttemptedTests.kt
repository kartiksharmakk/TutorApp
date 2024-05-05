package com.example.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Data.DataModel
import com.example.myapplication.databinding.CustomTestLayoutBinding

class AdapterAttemptedTests(val context: Context, val tests: List<DataModel.Test>, val onItemClick: (DataModel.Test) -> Unit): RecyclerView.Adapter<AdapterAttemptedTests.TestViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterAttemptedTests.TestViewHolder {
        val binding = CustomTestLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterAttemptedTests.TestViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: AdapterAttemptedTests.TestViewHolder, position: Int) {
        val test = tests[position]
        holder.bind(context, test)
    }

    override fun getItemCount(): Int {
        return tests.size
    }

    class TestViewHolder(val binding: CustomTestLayoutBinding, val onItemClick: (DataModel.Test) -> Unit): RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        fun bind(context: Context, test: DataModel.Test){
            binding.apply {
                txtCreatorName.text = test.creatorId
                txtTestName1.text = test.testId
                root.setOnClickListener {
                    onItemClick.invoke(test)
                }
            }
        }
        override fun onClick(p0: View?) {

        }

    }
}