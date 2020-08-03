package com.david.mypassbook.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

public final class DateUtils {

    public companion object {
        private lateinit var mContext: Context;
        const val FORMAT_MONTH = "MM";
        const val FORMAT_STRING_MONTH = "MMM";

        public fun init(context: Context) {
            mContext = context
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
            val format: String = SimpleDateFormat(FORMAT_MONTH, Locale.ENGLISH).format(Date())
            return format
        }

        fun getCurrentMonthString(): String {
            val format: String =
                SimpleDateFormat(FORMAT_STRING_MONTH, Locale.ENGLISH).format(Date())
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