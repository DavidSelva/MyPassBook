package com.david.mypassbook.ui.passbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.david.mypassbook.db.MoneyModel
import com.david.mypassbook.db.MyPassBookDatabase
import com.david.mypassbook.repository.MoneyRepository
import kotlinx.coroutines.launch


public class MoneyViewModel(application: Application) : AndroidViewModel(application) {
    var TAG: String = MoneyViewModel::class.java.simpleName
    private val moneyRepository: MoneyRepository

    // LiveData gives us updated words when they change.
    val allData: LiveData<List<MoneyModel>>

    init {
        val dao = MyPassBookDatabase.getDatabase(application).myPassBookDao()
        moneyRepository = MoneyRepository(dao)
        allData = moneyRepository.getAllData()
    }

    fun insert(moneyModel: MoneyModel) {
        viewModelScope.launch {
            moneyRepository.insert(moneyModel)
        }
    }

    fun getDataByMonth(dateQuery: String): LiveData<List<MoneyModel>> {
        return moneyRepository.getDataByMonth(dateQuery)
    }
}