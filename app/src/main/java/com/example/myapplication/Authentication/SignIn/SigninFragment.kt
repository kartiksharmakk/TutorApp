package com.example.myapplication.Authentication.SignIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.Authentication.SignUp.SignupFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSigninBinding

class SigninFragment : Fragment() {
    lateinit var binding: FragmentSigninBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater,container,false)
        binding.txtSignUp.setOnClickListener {
            signUp()
        }
        binding.txtForgotPassword.setOnClickListener {
            forgotPass()
        }
        binding.btnSignIn.setOnClickListener {
            login()
        }
        return binding.root
    }
    fun isEmpty(): Boolean{
        var empty = false
        if(binding.edtSignInEmail.text.trim().toString().isEmpty()){
            empty = true
            binding.edtSignInEmail.error = "Email is required"
        }
        if(binding.edtSignInPassword.text.trim().toString().isEmpty()){
            empty = true
            binding.edtSignInPassword.error = "Password is required"
        }
        return empty
    }
    fun login(){
        if(!isEmpty()){

        }
    }

    fun signUp(){
        val frag = requireActivity().supportFragmentManager.beginTransaction()
        frag.replace(R.id.authFrame, SignupFragment())
        frag.commit()
    }
    fun forgotPass(){
        val frag = requireActivity().supportFragmentManager.beginTransaction()
        frag.replace(R.id.authFrame, ForgotFragment())
        frag.commit()
    }
}