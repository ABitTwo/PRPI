package com.ana.projeto.prpi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ana.projeto.prpi.ui.Comentario
import com.bumptech.glide.Glide

class ComentAdpter(var listComent: ArrayList<Comentario>, var context : Context) : RecyclerView.Adapter<ComentAdpter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgUser = itemView.findViewById<ImageView>(R.id.coment_user)
        var txName = itemView.findViewById<TextView>(R.id.coment_user_name)
        var txCont = itemView.findViewById<TextView>(R.id.coment_text_coment)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false)
        return ComentAdpter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = listComent[position]

        Glide.with(context).load(currentItem.uimg).into(holder.imgUser)
        holder.txName.setText(currentItem.uname)
        holder.txCont.setText(currentItem.content)
    }

    override fun getItemCount(): Int {
        return listComent.size
    }
}