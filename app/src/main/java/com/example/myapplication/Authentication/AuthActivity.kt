package com.example.myapplication.Authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Authentication.SignIn.SigninFragment
import com.example.myapplication.Functions.CommonFunctions.loadFragmentFromActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragmentFromActivity(R.id.authFrame,SigninFragment(), supportFragmentManager)
    }
}