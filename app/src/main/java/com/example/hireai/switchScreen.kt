package com.example.hireai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class switchScreen : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_switch_screen)
        var screen_name = intent.getStringExtra("screenname")
        var drawerlayout = findViewById<DrawerLayout>(R.id.switchdrawer)
        var menubar = findViewById<ImageView>(R.id.menu_bar)
        val navbar = findViewById<NavigationView>(R.id.navigationview2)
        if (screen_name == "aboutscreen"){
            loadFragment(aboutFragment())
        }
        else{
            loadFragment(dashboard())
        }
        menubar.setOnClickListener{
            drawerlayout.openDrawer(GravityCompat.START)
        }
        navbar.setNavigationItemSelectedListener { item->

            when(item.itemId){

                R.id.home->{
                    onBackPressedDispatcher.onBackPressed()
//                    val intent = Intent(this,MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    startActivity(intent)
//                    finish()
                    true
                }
                R.id.logout->{
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this,loginScreen::class.java))
                    finish()
                    true
                }
                R.id.dashboard ->{
                    val intent = Intent(this,switchScreen::class.java)
                    intent.putExtra("screenname","dashboard")
                    startActivity(intent)
                    true
                }
                R.id.aboutus->{
                    val intent = Intent(this,switchScreen::class.java)
                    intent.putExtra("screenname","aboutscreen")
                    startActivity(intent)
                    true
                }
                R.id.history->{
                    Toast.makeText(this,"Working on it", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.share->{
                    val shareApp = Intent(Intent.ACTION_SEND).apply { type="text/plain"
                        putExtra(Intent.EXTRA_TEXT,"""
                        Turn Interview Fear Into Interview Flare 
                        Download HireAI : 
                    """.trimIndent())}
                    startActivity(Intent.createChooser(shareApp,"Share Via"))
                    true
                }
                else->false
            }
        }
    }

    private fun loadFragment(aboutFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView,aboutFragment)
        transaction.commit()
    }
}