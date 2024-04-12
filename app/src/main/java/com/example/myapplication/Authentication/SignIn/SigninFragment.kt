package com.example.myapplication.Authentication.SignIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.Authentication.SignUp.SignupFragment
import com.example.myapplication.Functions.CommonFunctions
import com.example.myapplication.Functions.CommonFunctions.getPermissions
import android.Manifest
import android.text.method.PasswordTransformationMethod
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Functions.CommonFunctions.getToastShort
import com.example.myapplication.Functions.CommonFunctions.loadFragmentFromFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSigninBinding

class SigninFragment : Fragment() {
    lateinit var binding: FragmentSigninBinding

    var isPassVisible = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater,container,false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        getPermissions(requireActivity(), arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.POST_NOTIFICATIONS,
        ))

        binding.imgShowPassSignIn.setOnClickListener {
            showHidePassowrd()
        }
        binding.txtSignUp.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
        binding.txtForgotSignIn.setOnClickListener {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }
        binding.btnSignIn.setOnClickListener {
            login()
        }
        return binding.root
    }
    fun isEmpty(): Boolean{
        var empty = false
        if(binding.edtEmailSignIn.text.trim().toString().isEmpty()){
            empty = true
            binding.edtEmailSignIn.error = "Email is required"
        }
        if(binding.edtPasswordSignIn.text.trim().toString().isEmpty()){
            empty = true
            binding.edtPasswordSignIn.error = "Password is required"
        }
        return empty
    }
    fun login(){
        if(!isEmpty()){
            val email = binding.edtEmailSignIn.text.toString().trim()
            val password = binding.edtPasswordSignIn.text.toString().trim()
            getToastShort(requireContext(),"Logging in as ${email}")
        }
    }

    fun showHidePassowrd(){
        if(isPassVisible){
            binding.imgShowPassSignIn.setImageResource(R.drawable.showpassword)
            binding.edtPasswordSignIn.transformationMethod = PasswordTransformationMethod.getInstance()
        }else{
            binding.imgShowPassSignIn.setImageResource(R.drawable.hidepassword)
            binding.edtPasswordSignIn.transformationMethod = null
        }
        isPassVisible = !isPassVisible
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        CommonFunctions.onRequestPermissionsResult(requireActivity(), requestCode, permissions, grantResults)
    }
}