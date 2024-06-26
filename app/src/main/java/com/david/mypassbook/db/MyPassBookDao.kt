package com.david.mypassbook.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MyPassBookDao {

    @Query("SELECT * FROM MONEY_TABLE ORDER BY _id")
    fun getAllData(): LiveData<List<MoneyModel>>

    @Query("SELECT * from MONEY_TABLE WHERE month = :month AND year =:year ORDER BY _id ASC")
    fun getTransactionsByMonth(month: String, year: String): LiveData<List<MoneyModel>>

    @Query("SELECT * FROM DAILY_EXPENSE_TABLE")
    fun getDailyExpenses(): LiveData<List<DailyExpenseModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDailyExpense(dailyExpenseModel: DailyExpenseModel)

    @Query("DELETE FROM DAILY_EXPENSE_TABLE WHERE _id = :expenseId")
    fun deleteDailyExpense(expenseId: Long?)

    @Query("SELECT COUNT(*) FROM MONEY_TABLE WHERE month = :str")
    fun getTransactionCount(str: String): Long

    @Query("SELECT * FROM MONEY_TABLE WHERE month = :str ORDER BY _id DESC LIMIT 1")
    fun getTotalByMonth(str: String): MoneyModel

    @Query("SELECT * FROM MONEY_TABLE ORDER BY _id ASC LIMIT 1")
    fun getFirstTransaction(): MoneyModel

    @Query("SELECT * FROM MONEY_TABLE ORDER BY _id DESC LIMIT 1")
    fun getLastTransaction(): MoneyModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(moneyModel: MoneyModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun editSalary(salaryModel: SalaryModel)

    @Query("SELECT * from SALARY_TABLE ORDER BY id DESC LIMIT 1")
    fun getCurrentSalary(): SalaryModel
}