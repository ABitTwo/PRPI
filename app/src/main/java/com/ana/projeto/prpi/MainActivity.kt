package com.ana.projeto.prpi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var list: ArrayList<ItemData>
    lateinit var adapter: RecyclerAdapter
    lateinit var firebaseDatabase: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        var v = intent.getStringExtra("spinner")

        recyclerView = findViewById(R.id.recyclerview)
        var layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager=layoutManager
        recyclerView.setHasFixedSize(true)

        if(v!!.contains("1")){
            firebaseDatabase = FirebaseDatabase.getInstance().reference.child("imageS")

        }else{
            firebaseDatabase = FirebaseDatabase.getInstance().reference.child("imageC")
        }


        list = ArrayList()

        var botao = findViewById<Button>(R.id.button2)

        botao.setOnClickListener{

            var intent = Intent(this, LoginActivity::class.java )
            startActivity(intent)
        }


    }

    override fun onStart() {
        super.onStart()

        firebaseDatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dataSnapshot in snapshot.children){
                    var data:ItemData?= dataSnapshot.getValue(ItemData::class.java)

                    list.add(data!!)

                }

                adapter=RecyclerAdapter(list,this@MainActivity)
                recyclerView.adapter=adapter

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@MainActivity,"error", Toast.LENGTH_LONG).show()

            }
        })

    }




}