package com.example.hireai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class historyAdapter(var historydata:List<scoredata>):RecyclerView.Adapter<myCardview>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myCardview {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.mypoints,parent,false)
        return myCardview(view)
    }

    override fun getItemCount(): Int {
        return historydata.size
    }

    override fun onBindViewHolder(holder: myCardview, position: Int) {
        var data = historydata[position]
        holder.scored.text = data.totalscored
        holder.datehistory.text = data.datescored
    }
}

class myCardview(View: View):RecyclerView.ViewHolder(View){
    var scored = View.findViewById<TextView>(R.id.points_history)
    var datehistory = View.findViewById<TextView>(R.id.date_history)
}