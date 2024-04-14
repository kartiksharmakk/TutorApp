package com.example.myapplication.Authentication.SignUp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import com.example.myapplication.Data.DataModel
import com.example.myapplication.Data.Prefs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentVerifyPhoneBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.Locale.IsoCountryCode

class VerifyPhoneFragment : Fragment() {
    lateinit var binding: FragmentVerifyPhoneBinding
    val viewModel: AuthenticationViewModel by activityViewModels<AuthenticationViewModel>()
    private lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    private lateinit var storedVerificationId: String
    private lateinit var email: String
    private lateinit var pass: String
    private lateinit var countryCode: String
    private lateinit var phoneNumber: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyPhoneBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = Firebase.database
        databaseReference = firebaseDatabase.getReference("User Details")
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
        val context = context ?: return
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if(it.isSuccessful){
                sendVerificationMail()
                Toast.makeText(context, "Sign up successful", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Error " + it.exception, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sendVerificationMail(){
        val user = auth?.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {
            try {
                if(it.isSuccessful){

                }else{

                }
            }catch (e: Exception){

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
                    saveUserDetails()
                    findNavController().navigate(R.id.action_verifyPhoneFragment_to_signInFragment)
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // Show toast message for invalid OTP
                        Toast.makeText(requireContext(), "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Show toast message for other verification failures
                        Toast.makeText(requireContext(), "Verification failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    fun saveUserDetails(){
        val email1 = encodeEmail(email)
        val phoneWithCountryCode = countryCode + phoneNumber
        val user = DataModel.UserCredentials(email1, countryCode, phoneNumber, false )
        val positionListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    databaseReference.child(email1).setValue(user)
                    Log.d("VerifyPhoneFragment","saveUserDetails: User data saved to firebase")
                }catch (e: Exception){
                    Log.d("VerifyPhoneFragment","Error : ${e.message}")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG,"positionListener:onCancelled",error.toException())
            }
        }
        databaseReference.addValueEventListener(positionListener)
    }
    fun encodeEmail(email: String): String {
        return email.replace(".", "(dot)")
    }
    fun Observer(){
        viewModel.phone.observe(viewLifecycleOwner){
            if(it != null){
                binding.txtForgetPasswordDialog.setText(getString(R.string.verifyPhone)+"\n$it")
                phoneNumber = it
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
        viewModel.countryCode.observe(viewLifecycleOwner){
            if(it != null){
                countryCode = it
            }
        }
    }

}