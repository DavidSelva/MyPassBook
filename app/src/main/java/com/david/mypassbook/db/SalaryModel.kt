package com.david.mypassbook.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Salary_Table")
class SalaryModel(
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "salary") val salary: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}