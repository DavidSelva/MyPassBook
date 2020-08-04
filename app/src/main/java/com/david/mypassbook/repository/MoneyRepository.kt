package com.david.mypassbook.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.david.mypassbook.db.*


class MoneyRepository(private val passBookDao: MyPassBookDao, private val mContext: Context) {
    private var TAG: String = MoneyRepository::javaClass.name
    lateinit var listLiveData: LiveData<List<MoneyModel>>

    /** Transaction Repository*/
    fun getAllData(): LiveData<List<MoneyModel>> {
        return passBookDao.getAllData()
    }

    fun getTransactionsByMonth(dateQuery: String): LiveData<List<MoneyModel>> {
        val dataByMonth: LiveData<List<MoneyModel>> = passBookDao.getTransactionsByMonth(dateQuery)
        listLiveData = dataByMonth
        return listLiveData
    }

    fun insertTransaction(money: MoneyModel) {
        passBookDao.insertTransaction(money)
    }

    /** Daily Expense Repository*/
    fun insertDailyExpense(dailyExpense: DailyExpenseModel) {
        passBookDao.insertDailyExpense(dailyExpense)
    }

    fun getAllDailyExpenses(): LiveData<List<DailyExpenseModel>> {
        return passBookDao.getDailyExpenses()
    }

    fun getFirstTransaction(): MoneyModel {
        return passBookDao.getFirstTransaction()
    }

    fun getLastTransaction(): MoneyModel {
        return passBookDao.getLastTransaction()
    }

    fun getTotalByMonth(currentMonth: String): MoneyModel {
        return passBookDao.getTotalByMonth(currentMonth)
    }

    fun getCurrentSalary(): SalaryModel {
        return passBookDao.getCurrentSalary()
    }

    fun insertSalary(salaryModel: SalaryModel) {
        passBookDao.insertSalary(salaryModel)
    }

    fun getTransactionCount(str: String): Long {
        return passBookDao.getTransactionCount(str)
    }

    fun clearAllTables() {
        MyPassBookDatabase.getDatabase(mContext).clearAllTables()
    }
}