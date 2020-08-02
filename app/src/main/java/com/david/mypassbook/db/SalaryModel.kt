package com.david.mypassbook.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Salary_Table")
class SalaryModel(month: String, salary: Double) {
    @PrimaryKey(autoGenerate = true)
    private var _id: Long? = null
    var month = ""
    var salary = java.lang.Double.valueOf(0.0)

    fun get_id(): Long? {
        return _id
    }

    fun set_id(l: Long?) {
        _id = l
    }
}