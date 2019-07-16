package com.jahanshahi.timeapp.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.jahanshahi.timeapp.R
import com.wang.avi.AVLoadingIndicatorView

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash)
        val avLoadingIndicatorView: AVLoadingIndicatorView = findViewById(R.id.avi)
        avLoadingIndicatorView.smoothToShow()
        Handler().postDelayed({
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            avLoadingIndicatorView.smoothToHide()
            finish()
        }, SPLASH_TIME_OUT)
    }
}
