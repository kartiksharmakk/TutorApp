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
import android.content.Intent
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Data.AuthenticationViewModel
import com.example.myapplication.Data.Prefs
import com.example.myapplication.Functions.CommonFunctions.getToastShort
import com.example.myapplication.Functions.CommonFunctions.loadFragmentFromFragment
import com.example.myapplication.R
import com.example.myapplication.Tutor.TutorHome
import com.example.myapplication.databinding.FragmentSigninBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.Locale.IsoCountryCode

class SigninFragment : Fragment() {
    lateinit var binding: FragmentSigninBinding
    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var countryCode: String
    lateinit var phoneNumber: String
    var isPassVisible = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = Firebase.database
        databaseReference = firebaseDatabase.getReference("User Details")

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
        binding.txtSendVerificationLink.setOnClickListener {
            sendVerificationLink()
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
                        updateVerificationInDatabase(true)
                        Prefs.getLoggedIn(requireContext(),true)
                        val intent = Intent(requireContext(), TutorHome::class.java)
                        startActivity(intent)
                        Toast.makeText(requireContext(),"Sign In successful",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(),"Please verify your email",Toast.LENGTH_SHORT).show()
                        binding.txtSendVerificationLink.visibility = View.VISIBLE
                    }
                }else{
                    Toast.makeText(requireContext(), "Failed to reload user", Toast.LENGTH_SHORT).show()

                }
            }catch (e: Exception){
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sendVerificationLink(){
        val user = auth?.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {
            try {
                if(it.isSuccessful){
                    Toast.makeText(requireContext(),"Mail sent",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),"Unable to send verification mail", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(requireContext(),"Error : ${e.message}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun encodeEmail(email: String): String {
        return email.replace(".", "(dot)")
    }

    fun updateVerificationInDatabase(status: Boolean){
        val email = binding.edtEmailSignIn.text.toString().trim()
        val email1 = encodeEmail(email)
        databaseReference.child(email1).child("verified").setValue(true)
        updateSharedPreferences(email1)
    }

    fun updateSharedPreferences(email: String){
        Prefs.saveUserEmailEncoded(requireContext(), email)
        databaseReference.child(email).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists() && snapshot.hasChild("uid")){
                    val uid = snapshot.child("uid").value.toString()
                    Prefs.saveUID(requireContext(),uid)
                }else{
                    Log.d("SignInFragment","Error in updating uid to SharedPreferences (updateSharedPreferences())")
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
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