package com.example.hireai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class loginScreen : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)
        val sign_up_redirect = findViewById<TextView>(R.id.signup_redirect)
        val login_btn = findViewById<ImageView>(R.id.login_btn)

        sign_up_redirect.setOnClickListener{
            startActivity(Intent(this,signupScreen::class.java))
            finish()
        }

        login_btn.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}