package com.ana.projeto.prpi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerAdapter (var list : ArrayList<ItemData>, var context: Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var designImage = itemView.findViewById<ImageView>(R.id.design_image)
        var designDesc = itemView.findViewById<TextView>(R.id.design_descricao)
        var designTitle = itemView.findViewById<TextView>(R.id.design_text)
        var imgDog = itemView.findViewById<ImageView>(R.id.image_dog)
        var imgCadeira = itemView.findViewById<ImageView>(R.id.image_cadeira)
        var imgOlho = itemView.findViewById<ImageView>(R.id.desig_olho)
        var imgOuvido = itemView.findViewById<ImageView>(R.id.img_ouvido)
        var imgBraile = itemView.findViewById<ImageView>(R.id.img_braile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.design_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var currentItem = list[position]

        Glide.with(context).load(currentItem.image).into(holder.designImage)
        Glide.with(context).load(currentItem.imageDog).into(holder.imgDog)
        Glide.with(context).load(currentItem.imageCadeira).into(holder.imgCadeira)
        Glide.with(context).load(currentItem.imageOlho).into(holder.imgOlho)
        Glide.with(context).load(currentItem.imageOuvido).into(holder.imgOuvido)
        Glide.with(context).load(currentItem.imageBraile).into(holder.imgBraile)

        holder.designTitle.text = currentItem.title
        holder.designDesc.text = currentItem.titleDesc
    }

    override fun getItemCount(): Int {
        return list.size
    }

}