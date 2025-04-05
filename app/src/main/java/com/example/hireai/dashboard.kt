package com.example.hireai

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class dashboard : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressNumber: TextView
    private lateinit var circularProgress: ProgressBar
    private lateinit var adapter: historyAdapter
    private var userscores = mutableListOf<scoredata>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var db = FirebaseFirestore.getInstance()
        var user = FirebaseAuth.getInstance()

        adapter = historyAdapter(userscores)
        recyclerView = view.findViewById<RecyclerView>(R.id.historyrecyclerview)
        progressNumber = view.findViewById<TextView>(R.id.current_score)
        circularProgress = view.findViewById<ProgressBar>(R.id.circularProgress)
//        usercurrentpoint.text =  Constant.usercurrentpoint.toString()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fetchdatafromdb(db,user)


        fun updateCircularProgress(number: Int, maxNumber: Int) {
            val progress = (number.toFloat() / maxNumber.toFloat()) * 100
            circularProgress.progress = progress.toInt()
            progressNumber.text = number.toString()
        }

            updateCircularProgress(Constant.usercurrentpoint,100)


    }

    private fun fetchdatafromdb(db: FirebaseFirestore, user: FirebaseAuth) {
        Log.d("Debug", "fetchdatafromdb() called")
        user.currentUser?.let {
            Log.d("Debug", "Current User UID: ${user.currentUser!!.uid}")
            db.collection("userscore").document(it.uid).collection("userscorehistory").orderBy("datescored")
                .addSnapshotListener{ snapshot , error ->
                    if(error!=null){
                        return@addSnapshotListener
                    }
                    if (snapshot == null) {
                        Log.d("FirestoreData", "Snapshot is NULL!")
                        return@addSnapshotListener
                    }

                    if (snapshot.isEmpty) {
                        Log.d("FirestoreData", "No data found in Firestore!")
                        return@addSnapshotListener
                    }
                    Log.d("FirestoreData", "Snapshot Triggered!")
                    if(snapshot!=null && !snapshot.isEmpty){
                        Log.d("FirestoreData", "Inside if!")
                        userscores.clear()
                        for(document in snapshot.documents){
                            Log.d("FirestoreData", "Inside for")
                            var ts = document.getString("totalscored")?:"Not Found"
                            var ds = document.getString("datescored")?: "Not Found"
                            Log.d("FirestoreData", "Fetched Data -> Score: $ts, Date: $ds")
                            userscores.add(scoredata(ts,ds))
                        }
                        adapter.notifyDataSetChanged()

                    }
                }
        }
    }

}

