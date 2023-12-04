package com.amikostudio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amikostudio.R
import com.amikostudio.databinding.ItemEposideBinding
import com.amikostudio.databinding.ItemrecyclerviewBinding
import com.amikostudio.retrofit.EposideList
import com.amikostudio.retrofit.WatchLaterListData
import com.bumptech.glide.Glide

class EpisodeAdapter (var context: Context, val eposideList: List<EposideList>): RecyclerView.Adapter<EpisodeAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ItemEposideBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeAdapter.ViewHolder {
        return ViewHolder(ItemEposideBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.mainTV.text = eposideList[position].title.toString()
        holder.binding.japinesTV.text = eposideList[position].title_japanese.toString()
        Glide.with(context).load(eposideList[position].image).centerCrop().placeholder(R.drawable.main_image).into(holder.binding.emimeIV)


    }

    override fun getItemCount(): Int {
        return eposideList.size
    }
}