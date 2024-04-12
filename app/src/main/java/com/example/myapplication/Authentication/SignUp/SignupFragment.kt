package com.example.myapplication.Authentication.SignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Data.AuthenticationViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding

    val viewModel: AuthenticationViewModel by activityViewModels<AuthenticationViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        return binding.root
    }
    fun isEmpty(): Boolean{
        var emp = false
        if(binding.edtEmailSignUp.text.toString().trim().isEmpty()){
            emp = true
            binding.edtEmailSignUp.error = "Required field"
        }
        if(binding.edtPhoneSignUp.text.toString().trim().isEmpty()){
            emp = true
            binding.edtPhoneSignUp.error = "Required field"
        }
        if(binding.edtPasswordSignUp.text.toString().trim().isEmpty()){
            emp = true
            binding.edtPasswordSignUp.error = "Required field"
        }
        if(binding.edtConfirmPasswordSignUp.text.toString().trim().isEmpty()){
            emp = true
            binding.edtConfirmPasswordSignUp.error = "Required field"
        }
        if(binding.edtPhoneSignUp.text.toString().trim().length != 10){
            emp = true
            binding.edtPhoneSignUp.error = "10 digits required"
        }
        if(binding.edtPasswordSignUp.text.toString() != "" &&
            binding.edtPasswordSignUp.text.toString().trim() != binding.edtConfirmPasswordSignUp.text.toString().trim()){
            binding.edtPasswordSignUp.error = "Passwords don't match"
            binding.edtConfirmPasswordSignUp.error = "Passwords don't match"
        }
        return emp
    }
    fun signUp(){
        if(!isEmpty()){
            val email = binding.edtEmailSignUp.text.toString().trim()
            val phone = binding.edtPhoneSignUp.text.toString().trim()
            val countryCode = binding.ccpSignUp.selectedCountryCodeWithPlus
            viewModel.updateCredentials(email, countryCode, phone)
            findNavController().navigate(R.id.signInFragment)
        }
    }
}