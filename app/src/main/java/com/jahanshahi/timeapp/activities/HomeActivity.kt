package com.jahanshahi.timeapp.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.jahanshahi.timeapp.R
import com.jahanshahi.timeapp.fragments.HistoryFragment
import com.jahanshahi.timeapp.fragments.HomeFragment
import com.jahanshahi.timeapp.fragments.ProfileFragment

class HomeActivity : AppCompatActivity() {
    private var transaction: FragmentTransaction? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home)
        val bottomNavigation = findViewById(R.id.home_navigation_bottom) as MeowBottomNavigation
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.profile_ic))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.home_ic))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.history_ic))
        bottomNavigation.show(2,true)
        transaction = supportFragmentManager.beginTransaction()
        transaction!!.replace(R.id.fragment_container, HomeFragment.newInstance())
        transaction!!.commit()
        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1->{
                    transaction = supportFragmentManager.beginTransaction()
                    transaction!!.replace(R.id.fragment_container, ProfileFragment.newInstance())
                }
                2->{
                    transaction = supportFragmentManager.beginTransaction()
                    transaction!!.replace(R.id.fragment_container, HomeFragment.newInstance())
                }
                3->{
                    transaction = supportFragmentManager.beginTransaction()
                    transaction!!.replace(R.id.fragment_container, HistoryFragment.newInstance())
                }
            }
            transaction!!.commit()
        }
    }
}
