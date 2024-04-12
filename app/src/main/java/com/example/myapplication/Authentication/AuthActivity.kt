package com.example.myapplication.Authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication.Authentication.SignIn.SigninFragment
import com.example.myapplication.Functions.CommonFunctions.loadFragmentFromActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_auth_host_fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}