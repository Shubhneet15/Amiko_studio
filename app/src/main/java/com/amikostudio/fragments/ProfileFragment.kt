package com.amikostudio.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.amikostudio.R
import com.amikostudio.activities.DashboardActivity
import com.amikostudio.activities.SplashActivity
import com.amikostudio.comman.PrefsManager
import com.amikostudio.databinding.EditProfileBottomsheetBinding
import com.amikostudio.databinding.FragmentProfileBinding
import com.amikostudio.retrofit.EditProfileResponse
import com.amikostudio.retrofit.RegisterResponse
import com.amikostudio.retrofit.RestClient
import com.amikostudio.retrofit.getProfilData
import com.amikostudio.retrofit.getProfileResponse
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ProfileFragment : BasePhotoUplaodFragment(), View.OnClickListener {

    lateinit var binding : FragmentProfileBinding

    lateinit var bind : EditProfileBottomsheetBinding


    var IMAGE: MultipartBody.Part? = null




    override fun getImage(uri: String?, data: Uri) {
        val profReq = File(uri).asRequestBody("multipart/form-data".toMediaTypeOrNull())
        IMAGE = MultipartBody.Part.createFormData("image", File(uri).name, profReq)
        Glide.with(this).load(uri).circleCrop().into(bind.ivnewprofile)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentProfileBinding.inflate(layoutInflater, container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listner()

        (requireActivity() as DashboardActivity).supportFragmentManager.setFragmentResultListener("refresh",viewLifecycleOwner
        ) { requestKey, result ->
            try {
                getprofile()
            } catch (e: Exception)
            { e.printStackTrace() } }

        getprofile()
    }

    private fun listner() {
        binding.favoriateRL.setOnClickListener(this)
        binding.watchRL.setOnClickListener(this)
        binding.saveRL.setOnClickListener(this)
        binding.changePasswordRL.setOnClickListener(this)
        binding.logoutRL.setOnClickListener(this)
        binding.backIV.setOnClickListener(this)
        binding.ivaddprofile.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
       when(v!!.id){
           R.id.backIV -> (requireActivity() as DashboardActivity).onBackPressed()
           R.id.favoriateRL ->(requireActivity() as DashboardActivity).loadFragment(FavouriteFragment())
           R.id.watchRL ->(requireActivity() as DashboardActivity).loadFragment(WatchLaterFragment())
           R.id.saveRL ->(requireActivity() as DashboardActivity).loadFragment(SaveFragment())
           R.id.changePasswordRL ->(requireActivity() as DashboardActivity).loadFragment(ChangePasswordFragment())
           R.id.logoutRL -> showAlertDialog()
           R.id.ivaddprofile -> bottomSheetDialog()

       }
    }

    private fun showAlertDialog(){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Logout")
        alertDialog.setMessage("Are you sure you want to Logout?")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            ApplicationGlobal.prefsManager.logout()
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }



    @RequiresApi(Build.VERSION_CODES.M)
    fun bottomSheetDialog() {

        val dialog = BottomSheetDialog(requireActivity())
        bind = EditProfileBottomsheetBinding.inflate(layoutInflater)
        (dialog).setContentView(bind.root)

        editGetprofile()

//        bind.ivcross.setOnClickListener {
//            dialog.dismiss()
//        }
        bind.btsave.setOnClickListener{
            edit_profile()
            dialog.dismiss()
        }

        bind.ivaddprofile.setOnClickListener{
            showImageDialog()
        }
        dialog.show()

    }



    fun edit_profile(){

        CommonMethods.showProgressDialog(requireActivity())

        val name = bind.etname.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        RestClient.get().edit_profile(
            name,
            IMAGE
        ).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.statusCode == 200) {
                            CommonMethods.showToast(requireActivity(), "Updated Successfully")


                            ApplicationGlobal.profile= response.body()!!.data
                            ApplicationGlobal.prefsManager.setProfile(Gson().toJson(ApplicationGlobal.profile!!))
                            (requireActivity() as DashboardActivity).supportFragmentManager.setFragmentResult(
                                "refresh",
                                Bundle()
                            )


                        } else CommonMethods.showToast(requireActivity(), response.body()!!.message)
                    }
                } catch (e:Exception){
                    CommonMethods.dismissProgressDialog()
//                    CommonMethods.showToast(requireActivity(),e.message.toString())
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(requireActivity(), "${t.message}")
            }
        })
    }


    fun getprofile(){

        CommonMethods.showProgressDialog(requireActivity())

        RestClient.get().getProfile().enqueue(object : retrofit2.Callback<getProfileResponse>{
            override fun onResponse(call: Call<getProfileResponse>, response: Response<getProfileResponse>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() != null){
                        if (response.body()!!.statusCode == 200){
                            setdata(response.body()!!.data)
                        }else CommonMethods.showToast(requireActivity(), response.body()!!.message)
                    }
                }catch (e: Exception){
                    CommonMethods.dismissProgressDialog()
//                    CommonMethods.showToast(requireActivity(),e.message.toString())
                }
            }
            override fun onFailure(call: Call<getProfileResponse>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(requireActivity(), "${t.message}")
            }
        })
    }

    fun setdata(data : getProfilData){
        binding.nameTV.setText(data.user_name)
//        binding..setText(data.email)
        Glide.with(requireActivity()).load(data.image_path).circleCrop().placeholder(R.drawable.placeholder).into(binding.ivnewprofile)
    }


    fun editGetprofile(){

        CommonMethods.showProgressDialog(requireActivity())

        RestClient.get().getProfile().enqueue(object : retrofit2.Callback<getProfileResponse>{
            override fun onResponse(call: Call<getProfileResponse>, response: Response<getProfileResponse>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() != null){
                        if (response.body()!!.statusCode == 200){
                            editsetdata(response.body()!!.data)
                        }else CommonMethods.showToast(requireActivity(), response.body()!!.message)
                    }
                }catch (e: Exception){
                    CommonMethods.dismissProgressDialog()
//                    CommonMethods.showToast(requireActivity(),e.message.toString())
                }
            }
            override fun onFailure(call: Call<getProfileResponse>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(requireActivity(), "${t.message}")
            }
        })
    }

    fun editsetdata(data : getProfilData){
        bind.etname.setText(data.user_name)
//        binding..setText(data.email)
        Glide.with(requireActivity()).load(data.image_path).circleCrop().placeholder(R.drawable.placeholder).into(bind.ivnewprofile)
    }

}