package com.david.mypassbook.utils

import android.content.Context
import android.widget.Toast
import kotlin.jvm.internal.Intrinsics




class AppUtils {

    public fun makeToast(message: String?) {
        Toast.makeText(contex, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG: String = AppUtils::class.java.simpleName
        var contex: Context? = null
        var instance: AppUtils? = null

        @Synchronized
        private fun createInstance(mContext: Context) {
            if (instance == null) {
                instance = AppUtils()
                contex = mContext
            }
        }

        fun getInstance(context: Context): AppUtils? {
            createInstance(context)
            return instance
        }
    }

}