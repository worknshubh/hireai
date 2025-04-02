package com.example.hireai

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
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

class MainActivity : AppCompatActivity() {
    private lateinit var generativeModel: GenerativeModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: myAdapter
    private lateinit var drawerlayout:DrawerLayout
    private var chatSession: Chat? = null
    var messagedata = mutableListOf<messagedata>()

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        loadmodel()
        var user_message = findViewById<EditText>(R.id.user_message_box)
        var user_message_send_btn = findViewById<ImageView>(R.id.user_message_send_btn)
        var menubar = findViewById<ImageView>(R.id.menu_bar)
        var nav_bar = findViewById<NavigationView>(R.id.navigationview)
        drawerlayout = findViewById(R.id.drawer_layout)
        recyclerView = findViewById(R.id.recyclerview)
        adapter = myAdapter(messagedata)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

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
            You are an interviewer and your name is HireAI and your company name is ShubhAgency. 
            Act accordingly, maintain a structured interview format, and talk formally. 
            Guide the interviewee step by step:
            1️tart with personal information.
            Then ask about their skills.
            Ask what post they are applying for.
            Guide them based on their answers.
            Question: $userMessage
        """.trimIndent()

        val response = chatSession?.sendMessage(formattedMessage)
        if (response != null) {
            if (!response.text.isNullOrEmpty()) {
                addmessage(messagedata(response.text.toString(),"bot", Timestamp.now()))
            }
        }

    }
    fun loadmodel(){
        generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = Constant.api_key
        )
    }
}