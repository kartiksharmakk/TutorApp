package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.myapplication.Authentication.AuthActivity
import com.example.myapplication.databinding.ActivitySplashBinding

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    lateinit var anim: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding.imgSplash.setImageResource(R.drawable.logo)
        anim = AnimationUtils.loadAnimation(applicationContext,R.anim.splash_anim)
        binding.imgSplash.startAnimation(anim)
        Handler().postDelayed({
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right,R.anim.zoom_out)
            finish()
        },3000)

    }
}