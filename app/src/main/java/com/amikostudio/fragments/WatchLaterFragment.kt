package com.amikostudio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.amikostudio.R
import com.amikostudio.activities.DashboardActivity
import com.amikostudio.adapter.MainAdapter
import com.amikostudio.adapter.WatchlaterAdapter
import com.amikostudio.databinding.FragmentWatchLaterBinding
import com.amikostudio.retrofit.AminoListResponse
import com.amikostudio.retrofit.Anime
import com.amikostudio.retrofit.RestClient
import com.amikostudio.retrofit.WatchLaterListData
import com.amikostudio.retrofit.WatchLaterListResponse
import retrofit2.Call
import retrofit2.Response


class WatchLaterFragment : Fragment(), View.OnClickListener {

    lateinit var binding : FragmentWatchLaterBinding

    var watchlaterAdapter : WatchlaterAdapter?= null

    var list: ArrayList<WatchLaterListData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchLaterBinding.inflate(layoutInflater, container, false)
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.watchlaterRV.layoutManager = GridLayoutManager(requireActivity(), 2)
        watchlaterAdapter = WatchlaterAdapter(requireActivity(),list)
        binding.watchlaterRV.adapter = watchlaterAdapter


        watchlist()
        listner()
    }

    private fun listner() {
        binding.backIV.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.backIV-> (requireActivity() as DashboardActivity).onBackPressed()
        }
    }


    fun watchlist(){

        CommonMethods.showProgressDialog(requireActivity())



        RestClient.get().getWatchLater().enqueue(object  : retrofit2.Callback<WatchLaterListResponse>{
            override fun onResponse(call: Call<WatchLaterListResponse>, response: Response<WatchLaterListResponse>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() != null){
                        list.clear()
                        list.addAll(response.body()!!.data)

                        if (list.size == 0){
                            binding.emptylistTV.visibility = View.VISIBLE
                        }else{
                            binding.emptylistTV.visibility = View.GONE
                        }

                        watchlaterAdapter!!.notifyDataSetChanged()
                    }
                }catch (e:Exception){
                    CommonMethods.dismissProgressDialog()
                    CommonMethods.showToast(requireActivity(),e.message.toString())
                }
            }

            override fun onFailure(call: Call<WatchLaterListResponse>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(requireActivity(), "${t.message}")
            }
        })

    }
}