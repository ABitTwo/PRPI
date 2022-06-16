package com.ana.projeto.prpi

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth


class PostAdapter(var listPost: ArrayList<Post>, var context : Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var titlePost = itemView.findViewById<TextView>(R.id.post_title)
        var imgPost = itemView.findViewById<ImageView>(R.id.post_image)
        var imgUser = itemView.findViewById<ImageView>(R.id.post_image_user)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.post_item,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = listPost[position]

        Glide.with(context).load(currentItem.foto).into(holder.imgPost)

        if(currentItem.userFoto != null){
            Glide.with(context).load(currentItem.userFoto).into(holder.imgUser)
        }else{
            Glide.with(context).load(R.drawable.userphoto).into(holder.imgUser)
        }

        holder.titlePost.text = currentItem.title


        holder.itemView.setOnClickListener{

            var intent = Intent(this.context, PostDetailActivity::class.java)


            intent.putExtra("title",listPost.get(position).title)
            intent.putExtra("postImage", listPost.get(position).foto)
            intent.putExtra("description",listPost.get(position).descricao)
            intent.putExtra("postKey", listPost.get(position).postKey)
            intent.putExtra("userPhoto",listPost.get(position).userFoto)

            intent.putExtra("postName",listPost.get(position).name)

            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return listPost.size
    }
}