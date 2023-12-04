package com.amikostudio.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.amikostudio.R
import com.amikostudio.comman.BaseResponse
import com.amikostudio.databinding.ActivityLoginBinding
import com.amikostudio.fragments.ApplicationGlobal
import com.amikostudio.fragments.CommonMethods
import com.amikostudio.retrofit.RegisterResponse
import com.amikostudio.retrofit.RestClient

import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding : ActivityLoginBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)








        binding.spamTV.text = "Don’t have an account?"
        binding.spamTV.append(" ")
        span("Sign up", binding.spamTV)
        listner()
    }








    private fun span(text: String, textView: TextView) {

        val span = Spannable.Factory.getInstance().newSpannable(text)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setHighlightColor(Color.TRANSPARENT);


        textView.setOnLongClickListener { false }

        val cs: ClickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@LoginActivity,R.color.violet)
                ds.isUnderlineText = false


            }
            override fun onClick(v: View) {
                startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
            }
        }
        span.setSpan(cs, 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.append(span)
    }

    private fun listner() {
//        binding.forgetPasswordTV.setOnClickListener(this)
        binding.signUpBT.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
       when(v!!.id){
           R.id.signUpBT -> if (validateInput()){
               login()
           }
//           R.id.forgetPasswordTV -> startActivity(Intent(this@LoginActivity,ForgotPasswordActivity::class.java))

       }
    }




    private fun validateInput(): Boolean {
        if (binding.emailET.text.toString() == "") {
            CommonMethods.showToast(this@LoginActivity, "Please Enter Email")
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailET.text.toString().trim()).matches()) {
            CommonMethods.showToast(this, "Invalid Email")
            return false
        }

        if (binding.passwordsET.text.toString() == "") {
            CommonMethods.showToast(this@LoginActivity, "Please Enter Password")
            return false
        }
        return true
    }


    fun login(){

        CommonMethods.showProgressDialog(this)

        val map: HashMap<String, Any> = HashMap()
        map["device_token"]= "fcmtoken"
        map["device_type"] = "0"
        map["email"] = binding.emailET.text.toString()
        map["password"] = binding.passwordsET.text.toString()

        RestClient.get().login(map).enqueue(object : retrofit2.Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() != null){
                        if (response.body()!!.statusCode  == 200){
                            ApplicationGlobal.sessionId = response.body()!!.data.remember_token.toString()
                            ApplicationGlobal.profile= response.body()!!.data
                            ApplicationGlobal.prefsManager.setProfile(Gson().toJson(ApplicationGlobal.profile!!))
                            ApplicationGlobal.prefsManager.setSessionId(ApplicationGlobal.sessionId)
                            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                            finish()
                        }
                        else if(response.body()!!.statusCode  == 201){
                            startActivity(
                                Intent(this@LoginActivity, OtpActivity::class.java)
                                    .putExtra("otp", response.body()!!.data.otp)
                                    .putExtra("email", response.body()!!.data.email)
                            )
                        }
                        else {
                            CommonMethods.showToast(this@LoginActivity, response.body()!!.message)
                        }
                    }
                }catch (e: Exception){
                    CommonMethods.dismissProgressDialog()
                    CommonMethods.showToast(this@LoginActivity, e.message.toString())
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(this@LoginActivity, "${t.message}")
            }
        })
    }

}