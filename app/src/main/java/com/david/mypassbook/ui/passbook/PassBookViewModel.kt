package com.david.mypassbook.ui.passbook

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.david.mypassbook.db.*
import com.david.mypassbook.repository.MoneyRepository
import kotlinx.coroutines.launch


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

    fun getTransactionsByMonth(dateQuery: String): LiveData<List<MoneyModel>> {
        allTransactions = moneyRepository.getTransactionsByMonth(dateQuery)
        return allTransactions
    }

    fun insertDailyExpense(moneyModel: DailyExpenseModel) {
        viewModelScope.launch {
            moneyRepository.insertDailyExpense(moneyModel)
        }
    }

    fun getAllDailyExpenses(): LiveData<List<DailyExpenseModel>> {
        return moneyRepository.getAllDailyExpenses()
    }

    fun getTotalByMonth(currentMonth: String): MoneyModel {
        return moneyRepository.getTotalByMonth(currentMonth)
    }

    fun getCurrentSalary(): SalaryModel {
        return moneyRepository.getCurrentSalary()
    }

    fun insertSalary(salaryModel: SalaryModel) {
        moneyRepository.insertSalary(salaryModel)
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