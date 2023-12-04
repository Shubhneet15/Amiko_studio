package com.amikostudio.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.amikostudio.R
import com.amikostudio.databinding.ActivitySplashBinding
import com.amikostudio.fragments.ApplicationGlobal

class SplashActivity : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Handler(Looper.myLooper()!!).postDelayed({
            if(ApplicationGlobal.sessionId == "")
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            finish()
        }, 2000)




    }
}