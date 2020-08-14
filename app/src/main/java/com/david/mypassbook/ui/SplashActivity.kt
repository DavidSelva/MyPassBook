package com.david.mypassbook.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.david.mypassbook.databinding.ActivitySplashBinding
import com.david.mypassbook.ui.login.LoginActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate: ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(inflate.root)
        Handler().postDelayed(Runnable {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }, 1000)
    }
}