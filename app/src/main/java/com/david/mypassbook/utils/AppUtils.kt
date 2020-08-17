package com.david.mypassbook.utils

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.david.mypassbook.R
import java.text.DecimalFormat


class AppUtils {

    companion object {
        private val TAG: String = AppUtils::class.java.simpleName
        var mContext: Context? = null
        lateinit var instance: AppUtils

        @Synchronized
        private fun createInstance(mContext: Context) {
            instance = AppUtils()
            this.mContext = mContext
        }

        fun getInstance(context: Context): AppUtils {
            createInstance(context)
            return instance
        }

        public fun formatPrice(price: Double): String {
            val decimalFormat = DecimalFormat("0.00")
            return decimalFormat.format(price)
        }
    }

    public fun makeToast(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    public fun getAppTypeFace(): Typeface? {
        var typeface: Typeface? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            typeface = mContext?.resources?.getFont(R.font.lusitana)
        } else {
            typeface = mContext?.let { ResourcesCompat.getFont(it, R.font.lusitana) }
        }
        return typeface
    }

}