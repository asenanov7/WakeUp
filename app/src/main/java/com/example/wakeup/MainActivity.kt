package com.example.wakeup

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.composition.presentation.WelcomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_container, getFirstFragment())
                .commit()
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return when {
            SDK_INT >= TIRAMISU -> {
                checkSelfPermission(READ_MEDIA_IMAGES) == PERMISSION_GRANTED && checkSelfPermission(POST_NOTIFICATIONS) == PERMISSION_GRANTED
            }
            else -> {
                checkSelfPermission(READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED
            }
        }
    }

    private fun getFirstFragment(): Fragment {
        return if (hasRequiredPermissions()) {
            WelcomeFragment()
        } else {
            GreetingFragment()
        }
    }

}

