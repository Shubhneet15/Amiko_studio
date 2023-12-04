package com.amikostudio.fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.amikostudio.R
import com.amikostudio.activities.DashboardActivity
import com.amikostudio.databinding.FragmentChangePasswordBinding
import com.amikostudio.retrofit.Common
import com.amikostudio.retrofit.RestClient
import com.amikostudio.retrofit.WatchLaterListResponse
import retrofit2.Call
import retrofit2.Response


class ChangePasswordFragment : Fragment(), View.OnClickListener {

    lateinit var binding : FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChangePasswordBinding.inflate(layoutInflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listner()
    }

    private fun listner() {
        binding.backIV.setOnClickListener(this)
        binding.resetBT.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.backIV -> (requireActivity() as DashboardActivity).onBackPressed()
            R.id.resetBT ->
                if (validateInput()){
                    changePssword()
                }

        }
    }

    private fun validateInput(): Boolean {



        if (binding.passwordsET.text.toString() == "") {
            CommonMethods.showToast(requireActivity(), "Please Enter Password")
            return false
        }

        if (binding.confirmPasswordsET.text.toString() == "") {
            CommonMethods.showToast(requireActivity(), "Please Confirm Password")
            return false
        }

        if (binding.passwordsET.text.toString() != (binding.confirmPasswordsET.text.toString())){
            CommonMethods.showToast(requireActivity(), "Enter Both Password Should be same")
            return false
        }
        return true
    }



    fun changePssword() {

        CommonMethods.showProgressDialog(requireActivity())

        val map: HashMap<String, Any> = HashMap()



        map["old_password"] = binding.oldPasswordsET.text.toString()
        map["new_password"] = binding.passwordsET.text.toString()


        RestClient.get().changePassword(map).enqueue(object : retrofit2.Callback<Common>{
            override fun onResponse(call: Call<Common>, response: Response<Common>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() !=null){
                        if (response.body()!!.statusCode  == 200){
//                            (requireActivity() as DashboardActivity).onBackPressed()
                            CommonMethods.showToast(requireActivity(), "Password Changed Successfully")
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