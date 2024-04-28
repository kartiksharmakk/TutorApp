package com.example.myapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Data.DataModel
import com.example.myapplication.R
import com.example.myapplication.interfaces.QuestionClickListener

class QuestionAdapter(val questions: MutableList<DataModel.Question>,
                      val onAddQuestionClicked: () -> Unit,
                      val onQuestionClickListener: QuestionClickListener
): RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    sealed class QuestionItem {
        data class Question(val question: DataModel.Question) : QuestionItem()
    }

    private var currentQuestions = mutableListOf<QuestionItem>()

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
        val edtMarks: EditText = itemView.findViewById(R.id.edtMarks)
        val spinnerCorrectOption: Spinner = itemView.findViewById(R.id.spinnerCorrectOption)

        val btnSaveQuestion: Button = itemView.findViewById(R.id.btnSaveQuestion)
        val btnEditQuestion: Button = itemView.findViewById(R.id.btnEditQuestion)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_question_create_layout, parent, false)
        return QuestionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return currentQuestions.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = currentQuestions[position]
        var correctAnswer = ""
        val options = arrayOf("Answer","Option 1", "Option 2", "Option 3", "Option 4")
        val adapter = ArrayAdapter<String>(holder.itemView.context, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinnerCorrectOption.adapter = adapter
        holder.spinnerCorrectOption.setSelection(0, false)

        holder.spinnerCorrectOption.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                correctAnswer = p0?.getItemAtPosition(p2) as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        holder.itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                onQuestionClickListener.onQuestionInteraction(question, position)
            }
        }

        holder.btnSaveQuestion.setOnClickListener {
            holder.btnSaveQuestion.visibility = View.GONE
            holder.btnEditQuestion.visibility = View.VISIBLE

            val question = holder.edtQuestion.text.toString().trim()
            val option1 = holder.edtOption1.text.toString().trim()
            val option2 = holder.edtOption2.text.toString().trim()
            val option3 = holder.edtOption3.text.toString().trim()
            val option4 = holder.edtOption4.text.toString().trim()
            val marks = holder.edtMarks.text.toString().trim()
            val answer = when(correctAnswer){
                options[1] -> {option1}
                options[2] -> {option2}
                options[3] -> {option3}
                options[4] -> {option4}
                else -> {""}
            }

            if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || marks == null || answer.isEmpty()) {
                // Show error message (e.g., Toast)
                return@setOnClickListener
            }

            holder.edtQuestion.isEnabled = false
            holder.edtOption1.isEnabled = false
            holder.edtOption2.isEnabled = false
            holder.edtOption3.isEnabled = false
            holder.edtOption4.isEnabled = false
            holder.spinnerCorrectOption.isEnabled = false
        }

        holder.btnEditQuestion.setOnClickListener {
            holder.btnEditQuestion.visibility = View.GONE
            holder.btnSaveQuestion.visibility = View.VISIBLE

            holder.edtQuestion.isEnabled = true
            holder.edtOption1.isEnabled = true
            holder.edtOption2.isEnabled = true
            holder.edtOption3.isEnabled = true
            holder.edtOption4.isEnabled = true
            holder.spinnerCorrectOption.isEnabled = true

        }
    }
}