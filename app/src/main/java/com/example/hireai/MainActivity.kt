package com.example.hireai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import com.google.ai.client.generativeai.Chat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var generativeModel: GenerativeModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: myAdapter
    private lateinit var drawerlayout:DrawerLayout
    private var chatSession: Chat? = null
    var userpoint:Int = 0
    var totalpointsscored:Int = 0
    var messagedata = mutableListOf<messagedata>()
    var username:String = ""
    @SuppressLint("WrongViewCast", "MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        loadmodel()
        var user_message = findViewById<EditText>(R.id.user_message_box)
        var user_message_send_btn = findViewById<ImageView>(R.id.user_message_send_btn)
        var menubar = findViewById<ImageView>(R.id.menu_bar)
        var nav_bar = findViewById<NavigationView>(R.id.navigationview)
        firestore = FirebaseFirestore.getInstance()
        drawerlayout = findViewById(R.id.drawer_layout)
        recyclerView = findViewById(R.id.recyclerview)
        adapter = myAdapter(messagedata)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

            FirebaseAuth.getInstance().currentUser?.let {
                firestore.collection("UserInfo").document(
                    it.uid).get().addOnSuccessListener { document ->
                        username = document.getString("username").toString()
                }
            }

        addmessage(messagedata(Constant.greet,"bot", Timestamp.now()))
        user_message_send_btn.setOnClickListener{
            if(user_message.text.toString().isNotEmpty()){
                addmessage(messagedata(user_message.text.toString(),"user", Timestamp.now()))
            }
            else{
                return@setOnClickListener
            }
            lifecycleScope.launch {
                sendtoai(user_message.text.toString())
            }
            user_message.text.clear()
        }
        menubar.setOnClickListener{
        drawerlayout.openDrawer(GravityCompat.START)
        }
        nav_bar.setNavigationItemSelectedListener { item->
            when(item.itemId){
                R.id.home->{
                    startActivity(Intent(this,MainActivity::class.java))
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
                    Toast.makeText(this,"Working on it",Toast.LENGTH_SHORT).show()
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



    private fun addmessage(message: messagedata) {
        messagedata.add(message)
        adapter.notifyItemInserted(messagedata.size-1)
        recyclerView.scrollToPosition(messagedata.size-1)
    }

    private suspend fun sendtoai(userMessage: String) {
        if (chatSession == null) {
            chatSession = generativeModel.startChat()
        }

        val formattedMessage = """
             Interview Flow:
        Greet Candidate $username
            ${Constant.guideline}
       
         $userMessage
        """.trimIndent()

        val response = chatSession?.sendMessage(formattedMessage)
        if (response != null) {
            val responsetext = response.text.toString()
            val extractedPoints = extractCurrent(responsetext) ?:userpoint
            val totalpoints = extractTotal(responsetext)?:0

            userpoint = extractedPoints
            totalpointsscored = totalpoints
            if (!response.text.isNullOrEmpty()) {
                addmessage(messagedata(response.text.toString(),"bot", Timestamp.now()))
            }
            Log.e("TAG", "sendtoai: $userpoint", )
            Constant.usercurrentpoint = userpoint
            Log.e("TAG", "sendtoai: $totalpointsscored", )
            if(totalpointsscored>0){

                FirebaseAuth.getInstance().currentUser?.let {
                    firestore.collection("userscore").document(
                        it.uid).collection("userscorehistory").add(scoredata(totalpointsscored.toString(), LocalDateTime.now().toString())).addOnSuccessListener {
                            Toast.makeText(this,"Added Successfully to Your Profile",Toast.LENGTH_SHORT)
                                .show()                    }
                        .addOnFailureListener {
                            Toast.makeText(this,"Failed to Add to Profile ",Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }
    }
    fun loadmodel(){
        generativeModel = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = Constant.api_key
        )
    }
    fun extractCurrent(input: String): Int? {
        val regex = "scored so far:\\s*(\\d+)".toRegex()
        return regex.find(input)?.groupValues?.get(1)?.toIntOrNull()
    }

    fun extractTotal(input: String): Int? {
        val regex = "Total Marks Scored:\\s*(\\d+)".toRegex()
        return regex.find(input)?.groupValues?.get(1)?.toIntOrNull()
    }
    override fun onResume() {
        super.onResume()
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START)
        }
    }
}