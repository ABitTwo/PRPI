package com.ana.projeto.prpi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ana.projeto.prpi.ui.Comentario
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PostDetailActivity : AppCompatActivity() {

    lateinit var postKey : String
    lateinit var rv : RecyclerView
    lateinit var adapter : ComentAdpter
    lateinit var dataBase : FirebaseDatabase
    lateinit var listComent : ArrayList<Comentario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        var w = window
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()


        var imgPost = findViewById<ImageView>(R.id.post_detail_image)
        var imgUserPost = findViewById<ImageView>(R.id.post_detail_user)
        var imgCurrentUser = findViewById<ImageView>(R.id.post_dt_current_user)
        var textPostTitle = findViewById<TextView>(R.id.post_detail_title)
        var textPostDesc = findViewById<TextView>(R.id.post_det_desc)
        var textDateName = findViewById<TextView>(R.id.post_dt_name)
        var butonComent = findViewById<Button>(R.id.post_det_button)
        var textComment = findViewById<EditText>(R.id.post_dt_coment)

        var user = FirebaseAuth.getInstance().currentUser
        dataBase = FirebaseDatabase.getInstance()
        rv = findViewById(R.id.rv_comment)

        butonComent.setOnClickListener{

            butonComent.visibility = View.INVISIBLE
            var ref = dataBase.getReference("comment").child(postKey).push()
            var coment = textComment.text.toString()
            var uId = user.uid
            var uname = user.displayName
            var uImg = user?.photoUrl.toString()


            var comment = Comentario(coment,uId,uImg,uname)

            ref.setValue(comment).addOnSuccessListener {
                iniRvComent()
                showMessage("Comentario Adicionado")
                textComment.setText("")
                butonComent.visibility = View.VISIBLE
            }.addOnFailureListener{
                showMessage("Falha para adicionar comentario: " + it.message)
            }

        }

        var postImage = intent.extras?.getString("postImage")
        Glide.with(this).load(postImage).into(imgPost)

        var postTitle = intent.extras?.getString("title")
        textPostTitle.setText(postTitle)

        var userPost = intent.extras?.getString("userPhoto")
        if(userPost != null){
            Glide.with(this).load(userPost).into(imgUserPost)
        }else{
            Glide.with(this).load(R.drawable.userphoto).into(imgUserPost)
        }


        var postDesc = intent.extras?.getString("description")
        textPostDesc.setText(postDesc)

        if(user.photoUrl != null){
            Glide.with(this).load(user.photoUrl).into(imgCurrentUser)
        }else{
            Glide.with(this).load(R.drawable.userphoto).into(imgCurrentUser)
        }



        postKey = intent.extras?.getString("postKey").toString()

        var name = intent.extras?.getString("postName")
        name = user.displayName
        textDateName.setText(name)

        iniRvComent();


    }

    override fun onStart() {
        super.onStart()



    }

    private fun iniRvComent() {
        rv.layoutManager =  LinearLayoutManager(this@PostDetailActivity)
        rv.setHasFixedSize(true)

        dataBase.reference.child("comment").child(postKey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listComent = ArrayList()
                for (dataSnapshot in snapshot.children){
                    var data:Comentario?= dataSnapshot.getValue(Comentario::class.java)

                    listComent.add(data!!)
                }

                adapter = ComentAdpter(listComent, this@PostDetailActivity)
                rv.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

                showMessage("Erro a listar comentario")

            }
        })


    }

    private fun showMessage(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show()
    }

}