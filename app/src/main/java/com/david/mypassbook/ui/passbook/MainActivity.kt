package com.david.mypassbook.ui.passbook

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.david.mypassbook.R
import com.david.mypassbook.databinding.ActivityMainBinding
import com.david.mypassbook.db.MoneyModel
import com.david.mypassbook.db.MyPassBookDao
import com.david.mypassbook.ui.BaseActivity
import com.david.mypassbook.ui.passbook.dailyexpense.DialogDailyExpense
import com.david.mypassbook.utils.Constants
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog
import java.util.*


class MainActivity : BaseActivity() {

    var TAG: String = MainActivity::javaClass.name
    lateinit var mainBinding: ActivityMainBinding;
    var calendar: Calendar? = null
    var context: Context? = null
    var currentMonth: String? = null
    var currentMonthString: String? = null
    private val dailyList: List<MoneyModel> = ArrayList()
    var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    var monthListener: MonthYearPickerDialog.OnDateSetListener? = null
    private var displayHeight = 0
    private var displayWidth = 0
    lateinit var moneyViewModel: MoneyViewModel
    var myTranxDao: MyPassBookDao? = null
    private val pageNumber = 0
    lateinit var tranxAdapter: TranxAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        mainBinding = inflate;
        setContentView(inflate.root)
        setSupportActionBar(mainBinding.toolbar)
        context = this
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayWidth = displayMetrics.widthPixels
        displayHeight = displayMetrics.heightPixels
        moneyViewModel = ViewModelProvider(this).get(MoneyViewModel::class.java)
        setAdapter()
        moneyViewModel.allTransactions.observe(this, androidx.lifecycle.Observer { dailyList ->
            dailyList.let { tranxAdapter.setData(dailyList) }
        })
    }

    private fun setAdapter() {
        tranxAdapter = TranxAdapter(this, dailyList)
        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        tranxAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnAddDailyExpense -> {
                val fragmentTransaction: FragmentTransaction =
                    supportFragmentManager.beginTransaction()
                val prev: Fragment? = supportFragmentManager.findFragmentByTag(this.TAG)
                if (prev != null) {
                    fragmentTransaction.remove(prev)
                }
                fragmentTransaction.addToBackStack(null)
                val dialogAddExpense = DialogDailyExpense()
                dialogAddExpense.setContext(this)
                dialogAddExpense.setFrom(Constants.TAG_ADD)
                dialogAddExpense.show(supportFragmentManager, this.TAG)
                return true
            }
            R.id.btnAddSalary -> {

            }
            R.id.btnCalendar -> {

            }
            R.id.btnClear -> {

            }
            R.id.btnDailyExpense -> {

            }
            R.id.btnEditSalary -> {

            }
            R.id.btnPrint -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}