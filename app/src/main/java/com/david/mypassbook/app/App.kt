package com.david.mypassbook.app

import android.app.Application
import com.david.mypassbook.utils.DateUtils
import com.david.mypassbook.utils.PrefUtils
import com.david.mypassbook.utils.StorageUtils
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
        DateUtils.init(this)
        PrefUtils.initPref(this)
        StorageUtils.init(this)
    }
}