package com.example.myapplication.Authentication.SignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.OtpAdapter
import com.example.myapplication.Data.AuthenticationViewModel
import com.example.myapplication.Data.Prefs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentVerifyPhoneBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerifyPhoneFragment : Fragment() {
    lateinit var binding: FragmentVerifyPhoneBinding
    val viewModel: AuthenticationViewModel by activityViewModels<AuthenticationViewModel>()
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var email: String
    private lateinit var pass: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyPhoneBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        Observer()
        binding.imgBackVerifyPhone.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.recyclerOtp.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerOtp.adapter = OtpAdapter(requireContext(), binding.recyclerOtp)
        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        return binding.root
    }

    fun signUp(){
        val otp = (binding.recyclerOtp.adapter as OtpAdapter).getOtp()
        retrieveVerificationId()
        verifyPhoneWithOtp(storedVerificationId, otp)
    }
    fun signUpWithEmail(){
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(requireContext(), "Sign up successful", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "Error " + it.exception, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun retrieveVerificationId(){
        storedVerificationId = Prefs.getVerificationId(requireContext())!!
    }

    fun verifyPhoneWithOtp(verificationId: String, code: String){
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signinWithPhoneAuthCredentials(credential)
    }

    fun signinWithPhoneAuthCredentials(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) {task->
                if (task.isSuccessful) {
                    signUpWithEmail()
                    findNavController().navigate(R.id.action_verifyPhoneFragment_to_signInFragment)
                } else {

                    Toast.makeText(requireContext(), "Verification failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun Observer(){
        viewModel.phone.observe(viewLifecycleOwner){
            if(it != null){
                binding.txtForgetPasswordDialog.setText(getString(R.string.verifyPhone)+"\n$it")
            }
        }
        viewModel.username.observe(viewLifecycleOwner){
            if(it != null){
                email = it
            }
        }
        viewModel.password.observe(viewLifecycleOwner){
            if(it != null){
                pass = it
            }
        }
    }

}