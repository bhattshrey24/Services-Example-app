package com.example.serviceexampleapp

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.serviceexampleapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater, null, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.startBackgroundServiceBtn.setOnClickListener {
            var serviceIntent = Intent(this, MyBackgroundService::class.java)
            startService(serviceIntent)
        }
        binding.startForegroundServiceBtn.setOnClickListener {
            val isRunning = isForegroundServiceRunning(MyForegroundService::class.java.name)
            if (!isRunning) { // i.e. since its foreground service which means it will keep
                // running even when user closed the application therefore before starting again we
                // check whether our foreground service (i.e. MyForegroundService) is already
                // running or not
                var serviceIntent = Intent(this, MyForegroundService::class.java)
                startForegroundService(serviceIntent) //@RequiresApi(Build.VERSION_CODES.O) is because of startForegroundService
            }
        }
    }

    private fun isForegroundServiceRunning(nameOfServiceToCheck: String): Boolean {
        var activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val listOfRunningServices = activityManager.getRunningServices(Integer.MAX_VALUE)
        for (runningService in listOfRunningServices) {
            if (nameOfServiceToCheck == runningService.service.className) {
                return true
            }
        }
        return false
    }

}