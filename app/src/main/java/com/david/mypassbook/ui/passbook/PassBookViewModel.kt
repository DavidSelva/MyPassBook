package com.david.mypassbook.ui.passbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.david.mypassbook.db.*
import com.david.mypassbook.repository.MoneyRepository


public class PassBookViewModel(application: Application) : AndroidViewModel(application) {
    var TAG: String = PassBookViewModel::class.java.simpleName
    private val moneyRepository: MoneyRepository

    // LiveData gives us updated words when they change.
    lateinit var allTransactions: LiveData<List<MoneyModel>>

    val dailyExpenses: LiveData<List<DailyExpenseModel>>
    val bookDao: MyPassBookDao = MyPassBookDatabase.getDatabase(application).myPassBookDao()

    init {
        moneyRepository = MoneyRepository(bookDao, application.applicationContext)
        dailyExpenses = moneyRepository.getAllDailyExpenses()
    }

    fun getRepository(): MoneyRepository {
        return moneyRepository
    }

    fun insertTransaction(moneyModel: MoneyModel) {
        moneyRepository.insertTransaction(moneyModel)
    }

    fun getTransactionsByMonth(month: String, year: String): LiveData<List<MoneyModel>> {
        allTransactions = moneyRepository.getTransactionsByMonth(month, year)
        return allTransactions
    }

    fun insertDailyExpense(moneyModel: DailyExpenseModel) {
        moneyRepository.insertDailyExpense(moneyModel)
    }

    fun getAllDailyExpenses(): LiveData<List<DailyExpenseModel>> {
        return moneyRepository.getAllDailyExpenses()
    }

    fun deleteDailyExpense(_id: Long?) {
        moneyRepository.deleteDailyExpense(_id)
    }

    fun getTotalByMonth(currentMonth: String): MoneyModel {
        return moneyRepository.getTotalByMonth(currentMonth)
    }

    fun getCurrentSalary(): SalaryModel {
        return moneyRepository.getCurrentSalary()
    }

    fun editSalary(salaryModel: SalaryModel) {
        moneyRepository.editSalary(salaryModel)
    }

    fun getFirstTransaction(): MoneyModel {
        return moneyRepository.getFirstTransaction()
    }

    fun getLastTransaction(): MoneyModel {
        return moneyRepository.getLastTransaction()
    }

    fun getTransactionCount(str: String): Long {
        return moneyRepository.getTransactionCount(str)
    }

    fun clearAllTables() {
        moneyRepository.clearAllTables()
    }
}