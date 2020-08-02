package com.david.mypassbook.repository

import androidx.lifecycle.LiveData
import com.david.mypassbook.db.MoneyModel
import com.david.mypassbook.db.MyPassBookDao


class MoneyRepository(private val dao: MyPassBookDao) {
    private var TAG: String = MoneyRepository::javaClass.name
    private var listLiveData: LiveData<List<MoneyModel>> = dao.getAllData()
    private var myTranxDao: MyPassBookDao = dao

    fun getAllData(): LiveData<List<MoneyModel>> {
        return myTranxDao.getAllData()
    }

    fun getDataByMonth(dateQuery: String): LiveData<List<MoneyModel>> {
        val dataByMonth: LiveData<List<MoneyModel>> = myTranxDao.getDataByMonth(dateQuery)
        listLiveData = dataByMonth
        return dataByMonth
    }

    fun insert(money: MoneyModel) {
        dao.insertMoney(money)
    }
}