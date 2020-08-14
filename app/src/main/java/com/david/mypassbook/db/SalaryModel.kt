package com.david.mypassbook.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Salary_Table")
class SalaryModel(month: String, salary: Double) {
    @PrimaryKey(autoGenerate = true)
    var _id: Long = 0
    var month = month
    var salary = salary

}