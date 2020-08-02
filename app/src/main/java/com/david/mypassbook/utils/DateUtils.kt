package com.david.mypassbook.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

public final class DateUtils {

   public companion object {
        private lateinit var mContext: Context;
        private val FORMAT_MONTH = "M";
        private val FORMAT_STRING_MONTH = "MMM";

        public fun init(context: Context) {
            mContext = context
        }

        fun getMContext(): Context {
            return mContext
        }

        fun setMContext(context: Context) {
            mContext = context
        }

        fun getFORMAT_MONTH(): String {
            return FORMAT_MONTH
        }

        fun getFORMAT_STRING_MONTH(): String {
            return FORMAT_STRING_MONTH
        }

        fun getCurrentDateTime(): String {
            val format: String = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH).format(Date())
            return format
        }

        fun getCurrentDate(): String {
            val format: String = SimpleDateFormat("dd", Locale.ENGLISH).format(Date())
            return format
        }

        fun getCurrentMonth(): String {
            val format: String = SimpleDateFormat(getFORMAT_MONTH(), Locale.ENGLISH).format(Date())
            return format
        }

        fun getCurrentMonthString(): String {
            val format: String =
                SimpleDateFormat(getFORMAT_STRING_MONTH(), Locale.ENGLISH).format(Date())
            return format
        }

        fun getCurrentYear(): String {
            val format: String = SimpleDateFormat("yyyy", Locale.ENGLISH).format(Date())
            return format
        }

        fun getCurrentTime(): String {
            val format: String = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date())
            return format
        }

   }
}