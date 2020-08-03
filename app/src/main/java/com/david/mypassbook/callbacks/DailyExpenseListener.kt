package com.david.mypassbook.callbacks

import com.david.mypassbook.db.DailyExpenseModel


interface DailyExpenseListener {
    fun onDailyExpenseAdded(list: List<DailyExpenseModel>)
}