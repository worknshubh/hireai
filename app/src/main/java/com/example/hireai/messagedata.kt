package com.example.hireai

import com.google.firebase.Timestamp

data class messagedata(
    var message:String = "",
    var role:String = "",
    var timestamp: Timestamp? = null
)
