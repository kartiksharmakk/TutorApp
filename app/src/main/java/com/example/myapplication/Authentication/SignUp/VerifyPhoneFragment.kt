package com.example.myapplication.Authentication.SignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.OtpAdapter
import com.example.myapplication.Data.AuthenticationViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentVerifyPhoneBinding
import com.google.api.Distribution.BucketOptions.Linear

class VerifyPhoneFragment : Fragment() {
lateinit var binding: FragmentVerifyPhoneBinding
val viewModel: AuthenticationViewModel by activityViewModels<AuthenticationViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyPhoneBinding.inflate(inflater, container, false)
        Observer()
        binding.imgBackVerifyPhone.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.recyclerOtp.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerOtp.adapter = OtpAdapter(requireContext(), binding.recyclerOtp)
        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        val otp = (binding.recyclerOtp.adapter as OtpAdapter).getOtp()

        return binding.root
    }

    fun signUp(){
        findNavController().navigate(R.id.action_verifyPhoneFragment_to_signInFragment)
    }
    fun Observer(){
        viewModel.phone.observe(viewLifecycleOwner){
            if(it != null){
                binding.txtForgetPasswordDialog.setText(getString(R.string.verifyPhone)+"\n$it")
            }
        }
    }

}