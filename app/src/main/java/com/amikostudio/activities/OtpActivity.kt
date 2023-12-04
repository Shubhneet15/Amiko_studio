package com.amikostudio.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.amikostudio.R
import com.amikostudio.databinding.ActivityOtpBinding
import com.amikostudio.fragments.CommonMethods
import com.amikostudio.retrofit.RegisterResponse
import com.amikostudio.retrofit.RestClient
import retrofit2.Call
import retrofit2.Response

class OtpActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding : ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        listner()
    }

    fun init(){
        binding.otp.setOTP(intent.getStringExtra("otp")!!.toString())
    }

    private fun listner() {
        binding.continueBt.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.continueBt -> if (validateInput()){
                verifyotp()
                }

        }
    }



    private fun validateInput(): Boolean {
        if (binding.otp.otp!! == "") {
            CommonMethods.showToast(this, "Please Enter OTP")
            return false
        }

        return true
    }

    fun verifyotp() {
        CommonMethods.showProgressDialog(this)
        val map: HashMap<String, Any> = HashMap()
        map["otp"] =binding.otp.otp!!
        map["email"] = intent.getStringExtra("email").toString()
        RestClient.get().otp(map).enqueue(object : retrofit2.Callback<RegisterResponse>{
            override fun onResponse(call: retrofit2.Call<RegisterResponse>, response: Response<RegisterResponse>
            ) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() != null){
                        if (response.body()!!.statusCode  == 200){


                                startActivity(
                                    Intent(this@OtpActivity, LoginActivity::class.java))
                                finishAffinity()
                                CommonMethods.showToast(this@OtpActivity,"OTP verified successfully")


                        }else CommonMethods.showToast(this@OtpActivity,response.body()!!.message)
                    }
                }catch (e: Exception){
                    CommonMethods.showToast(this@OtpActivity,e.message.toString())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(this@OtpActivity, "${t.message}")
            }
        })
    }
}