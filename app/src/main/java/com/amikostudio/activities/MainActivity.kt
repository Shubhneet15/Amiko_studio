package com.amikostudio.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.amikostudio.R
import com.amikostudio.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listner()
    }

    private fun listner() {
        binding.signInBT.setOnClickListener(this);
        binding.signUpBT.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
      when(v!!.id){
          R.id.signInBT -> startActivity(Intent(this@MainActivity,LoginActivity::class.java))
          R.id.signUpBT -> startActivity(Intent(this@MainActivity,RegisterActivity::class.java))
      }
    }
}