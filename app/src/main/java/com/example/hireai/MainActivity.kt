package com.example.hireai

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import com.google.ai.client.generativeai.Chat

class MainActivity : AppCompatActivity() {
    private lateinit var generativeModel: GenerativeModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: myAdapter
    private var chatSession: Chat? = null
    var messagedata = mutableListOf<messagedata>()

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        loadmodel()
        var user_message = findViewById<EditText>(R.id.user_message_box)
        var user_message_send_btn = findViewById<ImageView>(R.id.user_message_send_btn)
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