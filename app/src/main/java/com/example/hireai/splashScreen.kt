package com.example.hireai

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class splashScreen : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        firebaseAuth = FirebaseAuth.getInstance()
        var user = firebaseAuth.currentUser
        Handler(mainLooper).postDelayed({
            if(user!=null){
                if (user.isEmailVerified){
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
                else{
                    startActivity(Intent(this,loginScreen::class.java))
                    finish()
                }
            }
            else{
                startActivity(Intent(this,loginScreen::class.java))
                finish()
            }

        },2800)

    }
}