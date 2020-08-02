package com.david.mypassbook.utils

import android.content.Context
import android.widget.Toast


class AppUtils {

    public fun makeToast(message: String?) {
        Toast.makeText(contex, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG: String = AppUtils::class.java.simpleName
        var contex: Context? = null
        lateinit var instance: AppUtils

        @Synchronized
        private fun createInstance(mContext: Context) {
            if (instance == null) {
                instance = AppUtils()
                contex = mContext
            }
        }

        fun getInstance(context: Context): AppUtils {
            createInstance(context)
            return instance
        }
    }

}