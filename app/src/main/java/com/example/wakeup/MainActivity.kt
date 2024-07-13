package com.example.wakeup

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import com.example.composition.presentation.WelcomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission(activity = this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        showAgitation(requestCode, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (isServiceRunning(UploadMediaService::class.java).not()) {
                startForegroundService(Intent(this, UploadMediaService::class.java))
            }
            supportFragmentManager.beginTransaction().replace(R.id.main_container, WelcomeFragment()).commit()
        }
    }

    private fun requestPermission(activity: Activity) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.POST_NOTIFICATIONS)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        ActivityCompat.requestPermissions(activity, permission, 1)
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val componentName = ComponentName(this, serviceClass)
        for (runningService in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (runningService.service.className == componentName.className) {
                return true
            }
        }
        return false
    }

    private fun showAgitation(requestCode: Int, grantResults: IntArray) {
        val textView = findViewById<TextView>(R.id.give_perm)
        textView.isInvisible = requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

}

