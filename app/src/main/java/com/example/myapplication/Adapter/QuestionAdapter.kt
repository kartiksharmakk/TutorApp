package com.example.myapplication.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.myapplication.Data.DataModel
import com.example.myapplication.R

class QuestionAdapter(val questions: MutableList<DataModel.Question>, val onAddQuestionClicked: () -> Unit): RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    class QuestionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardQuestion: CardView = itemView.findViewById(R.id.cardQuestion)
        val cardOption1: CardView = itemView.findViewById(R.id.cardOption1)
        val cardOption2: CardView = itemView.findViewById(R.id.cardOption2)
        val cardOption3: CardView = itemView.findViewById(R.id.cardOption3)
        val cardOption4: CardView = itemView.findViewById(R.id.cardOption4)

        val edtQuestion: EditText = itemView.findViewById(R.id.edtQuestion)
        val edtOption1: EditText = itemView.findViewById(R.id.edtOption1)
        val edtOption2: EditText = itemView.findViewById(R.id.edtOption2)
        val edtOption3: EditText = itemView.findViewById(R.id.edtOption3)
        val edtOption4: EditText = itemView.findViewById(R.id.edtOption4)

        val rbOption1: RadioButton = itemView.findViewById(R.id.rbOption1)
        val rbOption2: RadioButton = itemView.findViewById(R.id.rbOption2)
        val rbOption3: RadioButton = itemView.findViewById(R.id.rbOption3)
        val rbOption4: RadioButton = itemView.findViewById(R.id.rbOption4)

        val radioButtons: Array<RadioButton> = arrayOf(
            itemView.findViewById(R.id.rbOption1),
            itemView.findViewById(R.id.rbOption2),
            itemView.findViewById(R.id.rbOption3),
            itemView.findViewById(R.id.rbOption4)
        )

        fun getSelectedRadioButtonIndex(): Int{
            for (i in radioButtons.indices){
                if (radioButtons[i].isChecked){
                    return i
                }
            }
            return -1
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}