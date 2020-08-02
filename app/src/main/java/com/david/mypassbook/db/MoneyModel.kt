package com.david.mypassbook.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "money_table")
class MoneyModel() {
    @PrimaryKey(autoGenerate = true)
    private var _id: Long? = null
    var credit = 0.0
    private var date: String
    private var dateTime: String
    var debit = 0.0
    private var month: String
    private var particular: String
    private var time: String
    var total = 0.0
    private var year: String
    fun get_id(): Long? {
        return _id
    }

    fun set_id(l: Long?) {
        _id = l
    }

    fun getDateTime(): String {
        return dateTime
    }

    fun setDateTime(str: String) {
        dateTime = str
    }

    fun getDate(): String {
        return date
    }

    fun setDate(str: String) {
        date = str
    }

    fun getMonth(): String {
        return month
    }

    fun setMonth(str: String) {
        month = str
    }

    fun getYear(): String {
        return year
    }

    fun setYear(str: String) {
        year = str
    }

    fun getTime(): String {
        return time
    }

    fun setTime(str: String) {
        time = str
    }

    fun getParticular(): String {
        return particular
    }

    fun setParticular(str: String) {
        particular = str
    }

    init {
        val str = ""
        dateTime = str
        date = str
        month = str
        year = str
        time = str
        particular = str
    }
}