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
import androidx.core.content.ContextCompat
import com.amikostudio.R
import com.amikostudio.databinding.ActivityRegisterBinding
import com.amikostudio.fragments.CommonMethods
import com.amikostudio.retrofit.RegisterResponse
import com.amikostudio.retrofit.RestClient
import retrofit2.Call
import retrofit2.Response


class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding : ActivityRegisterBinding



    var OTP = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)





        binding.spamTV.text = "Do have an account?"
        binding.spamTV.append(" ")
        span("Sign In", binding.spamTV)

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
                ds.color = ContextCompat.getColor(this@RegisterActivity,R.color.violet)
                ds.isUnderlineText = false


            }
            override fun onClick(v: View) {
                startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
                finish()
            }
        }
        span.setSpan(cs, 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.append(span)
    }

    private fun listner() {
        binding.signUpBT.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
       when(v!!.id){
           R.id.signUpBT-> if (validateInput()){
               register()
               }



       }
    }



    private fun validateInput(): Boolean {
        if (binding.nameET.text.toString() == "") {
            CommonMethods.showToast(this@RegisterActivity, "Please Enter Name")
            return false
        }
        if (binding.emailET.text.toString() == "") {
            CommonMethods.showToast(this@RegisterActivity, "Please Enter Email")
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailET.text.toString().trim()).matches()) {
            CommonMethods.showToast(this, "Invalid Email")
            return false
        }

        if (binding.passwordsET.text.toString() == "") {
            CommonMethods.showToast(this@RegisterActivity, "Please Enter Password")
            return false
        }

        if (binding.confirmPasswordsET.text.toString() == "") {
            CommonMethods.showToast(this@RegisterActivity, "Please Confirm Password")
            return false
        }

        if (binding.passwordsET.text.toString() != (binding.confirmPasswordsET.text.toString())){
            CommonMethods.showToast(this, "Enter Both Password Should be same")
            return false
        }
        return true
    }


    fun register(){

        CommonMethods.showProgressDialog(this)

        val map: HashMap<String, Any> = HashMap()

        map["user_name"] = binding.nameET.text.toString()
        map["email"] = binding.emailET.text.toString()
        map["password"] = binding.passwordsET.text.toString()
        map["confirm_password"] =  binding.confirmPasswordsET.text.toString()
        map["device_type"] = 0
        map["device_token"] =  "tytyt"

        RestClient.get().register(map).enqueue(object : retrofit2.Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                CommonMethods.dismissProgressDialog()
                try {
                    if (response.isSuccessful && response.body() != null){
                        if (response.body()!!.statusCode  == 200){
                            startActivity(
                                Intent(this@RegisterActivity,OtpActivity::class.java)
                                    .putExtra("otp", response.body()!!.data.otp)
                                    .putExtra("email", binding.emailET.text.toString()))
                        } else CommonMethods.showToast(this@RegisterActivity, response.body()!!.message)
                    }else CommonMethods.showToast(this@RegisterActivity, response.body()!!.message)
                }catch (e: Exception){
                    CommonMethods.dismissProgressDialog()
                    CommonMethods.showToast(this@RegisterActivity, e.message.toString())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                CommonMethods.dismissProgressDialog()
                CommonMethods.showToast(this@RegisterActivity, "${t.message}")

            }
        })
    }





}