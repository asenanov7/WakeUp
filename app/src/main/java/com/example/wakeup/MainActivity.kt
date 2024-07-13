package com.example.wakeup

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

    private fun userLogin() = SharedPreference(this).getUserName() != null

    private fun getFirstFragment(): Fragment {
        return if (userLogin()) {
            WelcomeFragment()
        } else {
            GreetingFragment()
        }
    }

}

