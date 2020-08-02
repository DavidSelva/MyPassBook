package com.david.mypassbook.ui.passbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.david.mypassbook.db.DailyExpenseModel
import com.david.mypassbook.db.MoneyModel
import com.david.mypassbook.db.MyPassBookDao
import com.david.mypassbook.db.MyPassBookDatabase
import com.david.mypassbook.repository.MoneyRepository
import kotlinx.coroutines.launch


public class MoneyViewModel(application: Application) : AndroidViewModel(application) {
    var TAG: String = MoneyViewModel::class.java.simpleName
    private val moneyRepository: MoneyRepository

    // LiveData gives us updated words when they change.
    val allTransactions: LiveData<List<MoneyModel>>
    val dailyExpenses: LiveData<List<DailyExpenseModel>>
    val bookDao: MyPassBookDao

    init {
        bookDao = MyPassBookDatabase.getDatabase(application).myPassBookDao()
        moneyRepository = MoneyRepository(bookDao)
        allTransactions = moneyRepository.getAllData()
        dailyExpenses = moneyRepository.getAllDailyExpenses()
    }

    fun getRepository(): MoneyRepository {
        return moneyRepository
    }

    fun insertTranX(moneyModel: MoneyModel) {
        viewModelScope.launch {
            moneyRepository.insertTransaction(moneyModel)
        }
    }

    fun getDataByMonth(dateQuery: String): LiveData<List<MoneyModel>> {
        return moneyRepository.getDataByMonth(dateQuery)
    }

    fun insertDailyExpense(moneyModel: DailyExpenseModel) {
        viewModelScope.launch {
            moneyRepository.insertDailyExpense(moneyModel)
        }
    }

    fun getAllDailyExpenses(): LiveData<List<DailyExpenseModel>> {
        return moneyRepository.getAllDailyExpenses()
    }
}