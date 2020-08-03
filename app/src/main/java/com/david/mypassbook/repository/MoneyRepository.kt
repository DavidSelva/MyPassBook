package com.david.mypassbook.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.david.mypassbook.db.*


class MoneyRepository(private val dao: MyPassBookDao, private val mContext: Context) {
    private var TAG: String = MoneyRepository::javaClass.name
    private var listLiveData: LiveData<List<MoneyModel>> = dao.getAllData()
    private var myTranxDao: MyPassBookDao = dao

    /** Transaction Repository*/
    fun getAllData(): LiveData<List<MoneyModel>> {
        return myTranxDao.getAllData()
    }

    fun getTransactionsByMonth(dateQuery: String): LiveData<List<MoneyModel>> {
        val dataByMonth: LiveData<List<MoneyModel>> = myTranxDao.getTransactionsByMonth(dateQuery)
        listLiveData = dataByMonth
        return dataByMonth
    }

    fun insertTransaction(money: MoneyModel) {
        dao.insertTransaction(money)
    }

    /** Daily Expense Repository*/
    fun insertDailyExpense(dailyExpense: DailyExpenseModel) {
        dao.insertDailyExpense(dailyExpense)
    }

    fun getAllDailyExpenses(): LiveData<List<DailyExpenseModel>> {
        return myTranxDao.getDailyExpenses()
    }

    fun getFirstTransaction(): MoneyModel {
        return myTranxDao.getFirstTransaction()
    }

    fun getLastTransaction(): MoneyModel {
        return myTranxDao.getLastTransaction()
    }

    fun getTotalByMonth(currentMonth: String): MoneyModel {
        return myTranxDao.getTotalByMonth(currentMonth)
    }

    fun getCurrentSalary(): SalaryModel {
        return myTranxDao.getCurrentSalary()
    }

    fun insertSalary(salaryModel: SalaryModel) {
        myTranxDao.insertSalary(salaryModel)
    }

    fun getTransactionCount(str: String):Long {
        return myTranxDao.getTransactionCount(str)
    }

    fun clearAllTables() {
        MyPassBookDatabase.getDatabase(mContext)
    }
}