package com.david.mypassbook.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Daily_Expense_Table")
class DailyExpenseModel {
    @PrimaryKey(autoGenerate = true)
    private var _id: Long? = null
    private var cost = 0.0
    private var expenseName = ""
    fun get_id(): Long? {
        return _id
    }

    fun set_id(l: Long?) {
        _id = l
    }

    fun getExpenseName(): String {
        return expenseName
    }

    fun setExpenseName(str: String?) {
        expenseName = str!!
    }

    fun getCost(): Double {
        return cost
    }

    fun setCost(d: Double?) {
        cost = d!!
    }
}