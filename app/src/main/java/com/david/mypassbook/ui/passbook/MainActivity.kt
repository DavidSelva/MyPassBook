package com.david.mypassbook.ui.passbook

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.david.mypassbook.databinding.ActivityMainBinding
import com.david.mypassbook.db.MoneyModel
import com.david.mypassbook.db.MyPassBookDao
import com.david.mypassbook.ui.BaseActivity
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
        moneyViewModel.allData.observe(this, androidx.lifecycle.Observer { dailyList ->
            dailyList.let { tranxAdapter.setData(dailyList) }
        })
    }

    private fun setAdapter() {
        tranxAdapter = TranxAdapter(this, dailyList)
        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        tranxAdapter.notifyDataSetChanged()
    }
}