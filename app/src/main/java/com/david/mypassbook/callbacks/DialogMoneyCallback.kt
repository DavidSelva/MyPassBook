package com.david.mypassbook.callbacks

import com.david.mypassbook.db.MoneyModel

interface DialogMoneyCallback {
    public fun onMoneyAdd(moneyModel: MoneyModel)
}