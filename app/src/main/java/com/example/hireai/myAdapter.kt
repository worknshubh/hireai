package com.example.hireai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class myAdapter(var messagedata: List<messagedata>): RecyclerView.Adapter<myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var view = inflater.inflate(R.layout.chats,parent,false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messagedata.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        var message = messagedata[position]
        if(message.role=="user"){
            holder.botlayout.visibility = View.GONE
            holder.userlayout.visibility = View.VISIBLE
            holder.usertext.text = message.message
        }
        else{
            holder.userlayout.visibility = View.GONE
            holder.botlayout.visibility = View.VISIBLE
            holder.bottext.text = message.message
        }
    }
}
class myViewHolder(View:View):RecyclerView.ViewHolder(View){
    var botlayout = View.findViewById<LinearLayout>(R.id.botmessagelayout)
    var bottext = View.findViewById<TextView>(R.id.botmessage)
    var userlayout = View.findViewById<LinearLayout>(R.id.usermessagelayout)
    var usertext = View.findViewById<TextView>(R.id.usermessage)

}