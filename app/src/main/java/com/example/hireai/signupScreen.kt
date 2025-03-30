package com.example.hireai

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import kotlin.math.log

class signupScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_screen)
        val login_redirect = findViewById<TextView>(R.id.login_redirect)
        val signup_btn = findViewById<ImageView>(R.id.signup_btn)

        login_redirect.setOnClickListener{
            startActivity(Intent(this,loginScreen::class.java))
            finish()
        }
        signup_btn.setOnClickListener{

        }
    }
}