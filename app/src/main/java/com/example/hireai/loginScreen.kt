package com.example.hireai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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

class loginScreen : AppCompatActivity() {
    private lateinit var user: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)
        val sign_up_redirect = findViewById<TextView>(R.id.signup_redirect)
        val login_btn = findViewById<ImageView>(R.id.login_btn)
        val buffer = findViewById<ProgressBar>(R.id.login_buffer)
        buffer.visibility= View.GONE
        user = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        sign_up_redirect.setOnClickListener{
            startActivity(Intent(this,signupScreen::class.java))
            finish()
        }

        login_btn.setOnClickListener{
            val usermail = findViewById<EditText>(R.id.user_email)
            val userpass = findViewById<EditText>(R.id.user_pass)
            if(validatedata(usermail,userpass)){
                buttonvisiblity(login_btn,"gone",buffer)

                    user.signInWithEmailAndPassword(usermail.text.toString(),userpass.text.toString()).addOnSuccessListener {
                        if(user.currentUser?.isEmailVerified == true){
                            Toast.makeText(this,"Logged In Successfully",Toast.LENGTH_SHORT).show()
                            buttonvisiblity(login_btn,"visible",buffer)
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this,"Please Verify Your Email First",Toast.LENGTH_SHORT).show()
                            buttonvisiblity(login_btn,"visible",buffer)
                        }
                    }
                        .addOnFailureListener {
                            exception->
                            Toast.makeText(this,"Unable to Login due to ${exception.message}",Toast.LENGTH_SHORT).show()
                            buttonvisiblity(login_btn,"visible",buffer)
                        }



            }
            else{
                buttonvisiblity(login_btn,"visible",buffer)
            }
        }
    }

    private fun buttonvisiblity(loginBtn: ImageView, visiblity: String, buffer: ProgressBar) {
        if(visiblity=="gone"){
            loginBtn.visibility = View.GONE
            buffer.visibility = View.VISIBLE
        }
        else if(visiblity=="visible"){
            loginBtn.visibility = View.VISIBLE
            buffer.visibility = View.GONE
        }
    }

    private fun validatedata(useremail: EditText, userpass: EditText):Boolean{
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