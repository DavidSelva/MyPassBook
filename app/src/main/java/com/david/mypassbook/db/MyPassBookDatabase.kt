package com.david.mypassbook.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(
    entities = [MoneyModel::class, SalaryModel::class, DailyExpenseModel::class],
    version = 1,
    exportSchema = false
)
abstract class MyPassBookDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: MyPassBookDatabase? = null

        fun getDatabase(context: Context): MyPassBookDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyPassBookDatabase::class.java,
                    "passbook_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun myPassBookDao(): MyPassBookDao

    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun clearAllTables() {
        TODO("Not yet implemented")
    }
}