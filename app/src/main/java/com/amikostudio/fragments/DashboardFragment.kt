package com.amikostudio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.amikostudio.activities.DashboardActivity
import com.amikostudio.adapter.MainAdapter
import com.amikostudio.databinding.FragmentDashboardBinding
import com.amikostudio.retrofit.AminoListResponse
import com.amikostudio.retrofit.Anime
import com.amikostudio.retrofit.RestClient
import retrofit2.Call
import retrofit2.Response


class DashboardFragment : Fragment() {

    lateinit var binding : FragmentDashboardBinding

    var mainAdapter : MainAdapter?= null

    var list: ArrayList<Anime> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dashboardRV.layoutManager = GridLayoutManager(requireActivity(), 2)
        mainAdapter = MainAdapter(requireActivity(), list)
        binding.dashboardRV.adapter = mainAdapter


        binding.profileIV.setOnClickListener {
            (requireActivity() as DashboardActivity).loadFragment(ProfileFragment())
        }

        Routienlist()
    }

    fun Routienlist(){

        CommonMethods.showProgressDialog(requireActivity())



        RestClient.get().AnimeList().enqueue(object  : retrofit2.Callback<AminoListResponse>{
            override fun onResponse(call: Call<AminoListResponse>, response: Response<AminoListResponse>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() != null){
                        list.clear()
                        list.addAll(response.body()!!.anime_list)
                        mainAdapter!!.notifyDataSetChanged()
                    }
                }catch (e:Exception){
                    CommonMethods.dismissProgressDialog()
                    CommonMethods.showToast(requireActivity(),e.message.toString())
                }
            }

            override fun onFailure(call: Call<AminoListResponse>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(requireActivity(), "${t.message}")
            }
        })

    }




}