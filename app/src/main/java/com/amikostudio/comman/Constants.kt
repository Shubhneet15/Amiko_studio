package com.amikostudio.fragments

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class Constants {
    companion object {
        const val APP_NAME = "Amiko studio"
        const val APP_TYPE = "0"


            var API_URL = "https://animate.cloudnex.in/api/"





        @RequiresApi(Build.VERSION_CODES.N)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf1 = SimpleDateFormat("EEE", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf2 = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf6 = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf5 = SimpleDateFormat("dd MMM, yyyy HH:mmaa", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf3 = SimpleDateFormat("HH:MM", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf18 = SimpleDateFormat("HH:MM:SS", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf4 = SimpleDateFormat("yyyy MM dd-HH:mm:ss", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf7 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf9 = SimpleDateFormat("dd-M-yyyy hh:mm", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf10 = SimpleDateFormat("dd MMM YYYY-hh:mm", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf12 = SimpleDateFormat("dd mm yyyy-hh:mm", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf11 = SimpleDateFormat("yyyy MM dd -hh:mm", Locale.getDefault())


        @RequiresApi(Build.VERSION_CODES.N)
        val sdf16 = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        val sdf17 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())


    }
}