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
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Functions.CommonFunctions.getToastShort
import com.example.myapplication.Functions.CommonFunctions.loadFragmentFromFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSigninBinding
import com.google.firebase.auth.FirebaseAuth

class SigninFragment : Fragment() {
    lateinit var binding: FragmentSigninBinding
    lateinit var auth: FirebaseAuth
    var isPassVisible = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
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
            signInRequest(email, password)
        }
    }

    fun signInRequest(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            try{
                if (it.isSuccessful){
                    checkVerificationStatus()
                }else{
                    Toast.makeText(requireContext(),"Please check your credentials",Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun checkVerificationStatus(){
        auth.currentUser?.reload()?.addOnCompleteListener {
            try{
                if (it.isSuccessful){
                    if(auth.currentUser!!.isEmailVerified){
                        Toast.makeText(requireContext(),"Sign In successful",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(),"Please verify your email",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), "Failed to reload user", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
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