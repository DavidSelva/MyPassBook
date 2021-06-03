package com.david.mypassbook.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.david.mypassbook.databinding.ActivitySplashBinding
import com.david.mypassbook.ui.login.LoginActivity
import com.david.mypassbook.utils.DateUtils
import timber.log.Timber

class SplashActivity : BaseActivity() {
    private val TAG = SplashActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate: ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(inflate.root)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }, 1000)
    }
}