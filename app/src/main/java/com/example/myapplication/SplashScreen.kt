package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.myapplication.Authentication.AuthActivity
import com.example.myapplication.Data.Prefs
import com.example.myapplication.Data.UserType
import com.example.myapplication.Student.StudentHome
import com.example.myapplication.Tutor.TutorHome
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
            val authIntent = Intent(this, AuthActivity::class.java)
            val tutorHomeIntent = Intent(this, TutorHome::class.java)
            val studentHomeIntent = Intent(this, StudentHome::class.java)
            val loggedIn = Prefs.isLoggedIn(this)
            if (loggedIn!!){
                if(Prefs.getUserType(this).equals(UserType.STUDENT.toString())){
                    startActivity(studentHomeIntent)
                }else{
                    startActivity(tutorHomeIntent)
                }
            }else{
                startActivity(authIntent)
            }
            overridePendingTransition(R.anim.slide_right,R.anim.zoom_out)
            finish()
        },3000)

    }
}