package com.david.mypassbook.callbacks;

import com.david.mypassbook.db.DailyExpenseModel;

import java.util.List;

/* compiled from: DailyExpenseListener.kt */
public interface DailyExpenseListener {
    void onDailyExpenseAdded(List<DailyExpenseModel> list);
}
