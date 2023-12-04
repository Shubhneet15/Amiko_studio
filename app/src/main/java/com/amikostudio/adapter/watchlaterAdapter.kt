package com.amikostudio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amikostudio.R
import com.amikostudio.activities.DashboardActivity
import com.amikostudio.databinding.ItemrecyclerviewBinding
import com.amikostudio.fragments.AmikoDetailFragment
import com.amikostudio.retrofit.Anime
import com.amikostudio.retrofit.WatchLaterListData
import com.bumptech.glide.Glide

class WatchlaterAdapter (var context: Context,val amikoList: List<WatchLaterListData>): RecyclerView.Adapter<WatchlaterAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ItemrecyclerviewBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {


                (context as DashboardActivity).loadFragment(AmikoDetailFragment(amikoList[position].anime_id.toInt()))

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlaterAdapter.ViewHolder {
        return ViewHolder(ItemrecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.binding.titleTV.text = amikoList[position].title.toString()
        Glide.with(context).load(amikoList[position].image).centerCrop().placeholder(R.drawable.main_image).into(holder.binding.imageView)

    }

    override fun getItemCount(): Int {
        return amikoList.size
    }
}