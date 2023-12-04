package com.amikostudio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amikostudio.R
import com.amikostudio.activities.DashboardActivity
import com.amikostudio.adapter.EpisodeAdapter
import com.amikostudio.adapter.SaveAdapter
import com.amikostudio.databinding.FragmentAmikoDetailBinding
import com.amikostudio.retrofit.AmiakoDetailResponse
import com.amikostudio.retrofit.AnimeList
import com.amikostudio.retrofit.Common
import com.amikostudio.retrofit.EposideList
import com.amikostudio.retrofit.RestClient
import com.amikostudio.retrofit.WatchLaterListData
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Response


class AmikoDetailFragment(var id1: Int) : Fragment(), View.OnClickListener {


    lateinit var binding : FragmentAmikoDetailBinding

    var episodeAdapter : EpisodeAdapter?= null

    var list: ArrayList<EposideList> = arrayListOf()

    var favouriate = ""
    var watchLater = ""
    var save = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAmikoDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.epsolideRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)

        episodeAdapter = EpisodeAdapter(requireActivity(), list)
        binding.epsolideRV.adapter = episodeAdapter


        animeDeatil()
        listner()


    }

    private fun listner() {
        binding.backIV.setOnClickListener(this)
        binding.favoriateLL.setOnClickListener(this)
        binding.watchLL.setOnClickListener(this)
        binding.saveLL.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
       when(p0!!.id){
           R.id.backIV ->(requireActivity() as DashboardActivity).onBackPressed()
           R.id.favoriateLL -> addFavouriate()
           R.id.watchLL ->watchLater()
           R.id.saveLL -> save()
       }
    }


    private fun addFavouriate() {
        if (favouriate == "1") {
            binding.favoriateIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.favourite_ac))
            favouriate = "2"
            addFavoriate(favouriate)
        } else {
            binding.favoriateIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.favourite_in))
            favouriate = "1"
            addFavoriate(favouriate)
        }
    }

    private fun watchLater() {
        if (watchLater == "1") {
            binding.watchLaterIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.watch_ac))
            watchLater = "2"
            addWatchLater(watchLater)
        } else {
            binding.watchLaterIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.watch_in))
            watchLater = "1"
            addWatchLater(watchLater)
        }
    }

    private fun save() {
        if (save == "1") {
            binding.saveIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.save_icon_ac))
            save = "2"
            addsave(save)
        } else {
            binding.saveIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.save_icon))
            save = "1"
            addsave(save)
        }
    }



    fun animeDeatil(){
        CommonMethods.showProgressDialog(requireActivity())
        val map: HashMap<String, Any> = HashMap()
        map["id"] = id1


        RestClient.get().AnimeDetail(map).enqueue(object : retrofit2.Callback<AmiakoDetailResponse>{
            override fun onResponse(call: Call<AmiakoDetailResponse>, response: Response<AmiakoDetailResponse>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() !=null){
                        setData(response.body()!!.anime_list)
                        list.clear()
                        list.addAll(response.body()!!.anime_list.data)
                        episodeAdapter!!.notifyDataSetChanged()
                    }else CommonMethods.showToast(requireActivity(), "Error")
                }catch (e: Exception){
                    CommonMethods.dismissProgressDialog()
                    CommonMethods.showToast(requireActivity(), e.message.toString())
                }
            }

            override fun onFailure(call: Call<AmiakoDetailResponse>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(requireActivity(), "${t.message}")

            }
        })
    }

    fun setData(data : AnimeList){

        binding.eposideTV.text = "Tv ${data.episodes}ps"
        binding.ratingTV.text = data.score
        binding.rankTV.text = "Ranked #${data.rank}"
        binding.titleTV.text = data.title
        binding.yearTV.text = "Fall ${data.year}"
        binding.detailTV.text = data.synopsis
        binding.sourceTV.text = data.source
        Glide.with(requireActivity()).load(data.image).centerCrop().placeholder(R.drawable.main_image).into(binding.amikoIV)


        if (data.favorites == "1"){
            binding.favoriateIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.favourite_in))
            favouriate = data.is_favourite.toString()
        }else{
            binding.favoriateIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.favourite_ac))
            favouriate = data.is_favourite.toString()
        }


        if (data.is_saved.toString() == "1"){
            binding.saveIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.watch_in))
            save = data.is_saved.toString()
        }else{
            binding.saveIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.watch_ac))
            save = data.is_saved.toString()
        }


        if (data.is_watched_later.toString() == "1") {
            binding.saveIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.save_icon))
           watchLater = data.is_watched_later.toString()
        } else {
            binding.saveIV.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.save_icon_ac))
            watchLater = data.is_watched_later.toString()
        }
    }


    fun addWatchLater(watchLater: String) {

        CommonMethods.showProgressDialog(requireActivity())

        val map: HashMap<String, Any> = HashMap()
        map["anime_id"] = id1
        map["type"] = watchLater


        RestClient.get().saved_to_watch_later(map).enqueue(object : retrofit2.Callback<Common>{
            override fun onResponse(call: Call<Common>, response: Response<Common>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() !=null){
                        if (response.body()!!.statusCode  == 200){

                        } else CommonMethods.showToast(requireActivity(), response.body()!!.message)
                    }else CommonMethods.showToast(requireActivity(), response.body()!!.message)
                }catch (e: Exception){
                    CommonMethods.dismissProgressDialog()
                    CommonMethods.showToast(requireActivity(), e.message.toString())
                }
            }

            override fun onFailure(call: Call<Common>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(requireActivity(), "${t.message}")

            }
        })
    }

    fun addsave(save: String) {

        CommonMethods.showProgressDialog(requireActivity())

        val map: HashMap<String, Any> = HashMap()



        map["anime_id"] = id1
        map["type"] = save


        RestClient.get().saved_to_save(map).enqueue(object : retrofit2.Callback<Common>{
            override fun onResponse(call: Call<Common>, response: Response<Common>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() !=null){
                        if (response.body()!!.statusCode  == 200){

                        } else CommonMethods.showToast(requireActivity(), response.body()!!.message)
                    }else CommonMethods.showToast(requireActivity(), response.body()!!.message)
                }catch (e: Exception){
                    CommonMethods.dismissProgressDialog()
                    CommonMethods.showToast(requireActivity(), e.message.toString())
                }
            }

            override fun onFailure(call: Call<Common>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(requireActivity(), "${t.message}")

            }
        })
    }

    fun addFavoriate(favouriate: String) {

        CommonMethods.showProgressDialog(requireActivity())

        val map: HashMap<String, Any> = HashMap()

        map["anime_id"] = id1
        map["type"] = favouriate


        RestClient.get().saved_to_favouriate(map).enqueue(object : retrofit2.Callback<Common>{
            override fun onResponse(call: Call<Common>, response: Response<Common>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() !=null){
                        if (response.body()!!.statusCode  == 200){

                        } else CommonMethods.showToast(requireActivity(), response.body()!!.message)
                    }else CommonMethods.showToast(requireActivity(), response.body()!!.message)
                }catch (e: Exception){
                    CommonMethods.dismissProgressDialog()
                    CommonMethods.showToast(requireActivity(), e.message.toString())
                }
            }

            override fun onFailure(call: Call<Common>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(requireActivity(), "${t.message}")

            }
        })
    }

}