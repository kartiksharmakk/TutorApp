package com.example.myapplication.Functions

import android.app.DatePickerDialog
import android.content.Context
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import java.util.Calendar

object CommonFunctions {
    fun getToastShort(context: Context, text: String){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
    fun getToastLong(context: Context, text: String){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun getEditTextLength(editText: EditText): Int{
        return editText.text.toString().trim().length
    }

    fun dropNavBar(window: WindowManager){

    }

    fun handleKeyboardInFragment(fragment: Fragment, scrollView: NestedScrollView, ){
        val activity = fragment.requireActivity()
        val softInputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val rootView = fragment.requireView().rootView

        rootView.viewTreeObserver.addOnGlobalLayoutListener (object: ViewTreeObserver.OnGlobalLayoutListener{
            private var rootHeight = 0

            override fun onGlobalLayout() {
                val viewHeight = rootView.height
                if(rootHeight == 0){
                    rootHeight = viewHeight
                }

                val heightDifference = rootHeight - viewHeight

                if(heightDifference > 100){
                    softInputManager.showSoftInput(fragment.requireView(), InputMethodManager.SHOW_FORCED)
                    scrollView.smoothScrollTo(0, heightDifference)
                }else{
                    scrollView.smoothScrollTo(0,0)
                }
            }

        } )
    }

    fun showDatePicker(context: Context,editText: EditText){
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var datePickerDialog = DatePickerDialog(context,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                editText.setText(date)
            },year,month,day)
        datePickerDialog.show()
    }
}