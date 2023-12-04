package com.amikostudio.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amikostudio.R
import com.amikostudio.adapter.MainAdapter
import com.amikostudio.databinding.ActivityDashboardBinding
import com.amikostudio.fragments.DashboardFragment


class DashboardActivity : AppCompatActivity() {


    lateinit var binding : ActivityDashboardBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)



        loadFragmentBackOff(DashboardFragment())

    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStackImmediate()
        else super.onBackPressed()
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment)
            .addToBackStack("").commit()
    }


    fun loadFragmentBackOff(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment)
            .disallowAddToBackStack().commit()
    }
}