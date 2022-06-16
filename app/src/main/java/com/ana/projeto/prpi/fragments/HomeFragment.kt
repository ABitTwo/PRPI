package com.ana.projeto.prpi.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ana.projeto.prpi.*
import com.ana.projeto.prpi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    lateinit var recycler : RecyclerView
    lateinit var adapter: PostAdapter
    lateinit var dataReference : DatabaseReference
    lateinit var listPost : ArrayList<Post>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var fragment = inflater.inflate(R.layout.fragment_home2,container,false)
        recycler = fragment.findViewById(R.id.pos_rv)
        var layoutManager = LinearLayoutManager(activity)
        recycler.layoutManager = layoutManager
        recycler.setHasFixedSize(true)
        dataReference = FirebaseDatabase.getInstance().reference.child("post")
        return fragment
    }

    override fun onStart() {
        super.onStart()

        dataReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                listPost = ArrayList()
                for (dataSnapshot in snapshot.children){
                    var data:Post?= dataSnapshot.getValue(Post::class.java)
                    listPost.add(data!!)
                }

                adapter = PostAdapter(listPost, activity!!)
                recycler.adapter = adapter



            }

            override fun onCancelled(error: DatabaseError) {

//                Toast.makeText(this,"error", Toast.LENGTH_LONG).show()

            }
        })


    }

}