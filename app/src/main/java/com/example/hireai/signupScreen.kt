package com.example.hireai

import android.content.Intent
import android.os.Bundle
import android.os.PatternMatcher
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.intellij.lang.annotations.Pattern
import org.w3c.dom.Text
import kotlin.math.log

class signupScreen : AppCompatActivity() {
    private lateinit var user:FirebaseAuth
    private lateinit var db:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_screen)
        val login_redirect = findViewById<TextView>(R.id.login_redirect)
        val signup_btn = findViewById<ImageView>(R.id.signup_btn)
        var buffer = findViewById<ProgressBar>(R.id.buffer)
        buffer.visibility = View.GONE
        login_redirect.setOnClickListener{
            startActivity(Intent(this,loginScreen::class.java))
            finish()
        }
        user = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        signup_btn.setOnClickListener{
            val username = findViewById<EditText>(R.id.username)
            val useremail = findViewById<EditText>(R.id.useremail)
            val userpass = findViewById<EditText>(R.id.userpass)
            buttonvisiblity(signup_btn,"visible",buffer)
            if(validatedata(username,useremail,userpass)){
                buttonvisiblity(signup_btn,"gone",buffer)
                user.createUserWithEmailAndPassword(useremail.text.toString(),userpass.text.toString()).addOnSuccessListener {
                    Toast.makeText(this,"Account created successfully! Please check your email for verification.",Toast.LENGTH_SHORT).show()
                    var userinfo = hashMapOf<String,String>()
                    userinfo["username"]=username.text.toString()
                    userinfo["usermail"]=useremail.text.toString()

                    user.currentUser?.let { it1 -> db.collection("UserInfo").document(it1.uid).set(userinfo) }
                    user.currentUser?.sendEmailVerification()
                    buttonvisiblity(signup_btn,"visible",buffer)
                    startActivity(Intent(this,loginScreen::class.java))
                    finish()
                }
                    .addOnFailureListener{message->
                        Toast.makeText(this,"Unable to Create Account due to ${message.message}",Toast.LENGTH_SHORT).show()
                        buttonvisiblity(signup_btn,"visible",buffer)
                    }
            }
            else{
                buttonvisiblity(signup_btn,"visible",buffer)
            }


        }
    }

    private fun buttonvisiblity(signupBtn: ImageView, visiblity: String,buffer:ProgressBar) {
            if(visiblity=="gone"){
                signupBtn.visibility = View.GONE
                buffer.visibility = View.VISIBLE
            }
            else if(visiblity=="visible"){
                signupBtn.visibility = View.VISIBLE
                buffer.visibility = View.GONE
            }
    }

    private fun validatedata(username: EditText, useremail: EditText, userpass: EditText):Boolean {
        if(username.text.toString().length<3){
            username.setError("Username too short! Must be at least 4 characters.")
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(useremail.text.toString()).matches()){
            useremail.setError("Invalid email! Please enter a valid email address.")
            return false
        }
        if(userpass.text.toString().length<6){
            userpass.setError("Userpass too short! Must be at least 7 characters.")
            return false
        }

        return true
    }


}