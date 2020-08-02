package com.david.mypassbook.utils

import android.content.Context
import android.content.SharedPreferences


class PrefUtils {

    companion object {
        private val TAG: String = PrefUtils::class.java.simpleName
        var mContext: Context? = null
        var instance: PrefUtils? = null
        const val KEY_IS_LOGGED = "KEY_IS_LOGGED"
        const val KEY_PIN = "KEY_PIN"
        const val PREF_NAME = "PREF_NAME"
        const val PRIVATE_MODE = 0
        var pref: SharedPreferences? = null

        fun initPref(context: Context) {
            val sharedPreferences =
                context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            pref = sharedPreferences
        }

        fun writeString(key: String, value: String) {
            pref?.edit()?.putString(key, value)?.apply()
        }

        fun readString(key: String): String? {
            return pref?.getString(key, "")
        }

        fun writeBoolean(key: String?, value: Boolean) {
            val editor = pref!!.edit()
            editor.putBoolean(key, value).apply()
        }

        fun readBoolean(key: String?): Boolean {
            return pref!!.getBoolean(key, false)
        }

    }
}