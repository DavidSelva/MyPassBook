package com.david.mypassbook.ui.passbook

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.DisplayMetrics
import android.util.LruCache
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.MeasureSpec
import android.widget.DatePicker
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.david.mypassbook.R
import com.david.mypassbook.callbacks.DialogMoneyCallback
import com.david.mypassbook.databinding.ActivityMainBinding
import com.david.mypassbook.db.DailyExpenseModel
import com.david.mypassbook.db.MoneyModel
import com.david.mypassbook.ui.BaseActivity
import com.david.mypassbook.ui.passbook.dailyexpense.DialogDailyExpense
import com.david.mypassbook.ui.passbook.pdfviewer.PdfViewActivity
import com.david.mypassbook.ui.passbook.salary.DialogEditSalary
import com.david.mypassbook.utils.AppUtils
import com.david.mypassbook.utils.Constants
import com.david.mypassbook.utils.DateUtils
import com.david.mypassbook.utils.StorageUtils
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


class MainActivity : BaseActivity(), DialogMoneyCallback {

    var TAG: String = MainActivity::javaClass.name
    lateinit var mainBinding: ActivityMainBinding;
    var calendar: Calendar = Calendar.getInstance()
    lateinit var mContext: Context
    lateinit var currentMonth: String
    lateinit var currentYear: String
    lateinit var currentMonthString: String
    lateinit var currentYearString: String
    private val dailyList: List<MoneyModel> = ArrayList()
    var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    lateinit var monthYearPickerDialog: MonthYearPickerDialogFragment
    var monthListener: MonthYearPickerDialog.OnDateSetListener? = null
    private var displayHeight = 0
    private var displayWidth = 0
    lateinit var passbookViewModel: PassBookViewModel
    private val pageNumber = 0
    lateinit var tranxAdapter: TranxAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        mainBinding = inflate;
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)

        mContext = this
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayWidth = displayMetrics.widthPixels
        displayHeight = displayMetrics.heightPixels
        setAdapter()
        passbookViewModel =
            ViewModelProvider(this@MainActivity).get(
                PassBookViewModel::class.java
            )
        currentMonth = DateUtils.getCurrentMonth()
        currentMonthString = DateUtils.getCurrentMonthString()
        currentYear = DateUtils.getCurrentYear()
        getTransactionsByMonth(currentMonth, currentYear);

//        initDefaultCalendar()
        initMonthAndYearCalendar()

        mainBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0 && !mainBinding.btnAddTranx.isShown)
                    btnAddTranx.show();
                else if (dy > 0 && btnAddTranx.isShown)
                    btnAddTranx.hide();
            }
        })

        mainBinding.btnAddTranx.setOnClickListener(View.OnClickListener {
            val fragmentTransaction: FragmentTransaction =
                supportFragmentManager.beginTransaction()
            val prev: Fragment? = supportFragmentManager.findFragmentByTag(TAG)
            if (prev != null) {
                fragmentTransaction.remove(prev)
            }
            fragmentTransaction.addToBackStack(null)
            val dialogAddMoney = DialogAddMoney()
            dialogAddMoney.show(supportFragmentManager, TAG)
        })
    }

    private fun getTransactionsByMonth(month: String, year: String) {
        Timber.tag(TAG).d("getTransactionsByMonth: %s", month)
        passbookViewModel.getTransactionsByMonth(month, year)
            .observe(this, androidx.lifecycle.Observer { dailyList ->
                Timber.tag(TAG).d("getTransactionsByMonth: %s", Gson().toJson(dailyList))
                if (dailyList != null) {
                    tranxAdapter.setData(dailyList)
                }
            })
    }

    private fun setAdapter() {
        tranxAdapter = TranxAdapter(this, dailyList)
        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tranxAdapter
        tranxAdapter.notifyDataSetChanged()
    }

    fun initDefaultCalendar() {
        calendar = Calendar.getInstance()
        dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(DateUtils.FORMAT_MONTH, Locale.US)
                val yearDateFormat = SimpleDateFormat(DateUtils.FORMAT_YEAR, Locale.US)
                val stringFormat = SimpleDateFormat(DateUtils.FORMAT_STRING_MONTH, Locale.US)
                currentMonthString = stringFormat.format(calendar.time)
                currentYearString = calendar.get(Calendar.YEAR).toString()
                currentMonth = sdf.format(calendar.time)
                currentYear = yearDateFormat.format(calendar.time)
                getTransactionsByMonth(currentMonth, currentYear)
            }
    }

    fun initMonthAndYearCalendar() {
        val yearSelected: Int
        val monthSelected: Int
        //Set default values
        val calendar = Calendar.getInstance()
        yearSelected = calendar[Calendar.YEAR]
        monthSelected = calendar[Calendar.MONTH]
        Executors.newSingleThreadExecutor().execute(
            Runnable {
                val firstData: MoneyModel = passbookViewModel.getFirstTransaction()
                if (firstData != null) {
                    calendar.clear()
                    calendar.set(Calendar.YEAR, firstData.getYear().toInt())
                    calendar.set(Calendar.MONTH, firstData.getMonth().toInt())
                    calendar.set(Calendar.DAY_OF_MONTH, 0)
                    calendar.set(Calendar.HOUR, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    val minDate = calendar.timeInMillis
                    val maxDate = System.currentTimeMillis()
                    monthYearPickerDialog = MonthYearPickerDialogFragment
                        .getInstance(monthSelected, yearSelected, minDate, maxDate, "")
                } else {
                    val minDate = System.currentTimeMillis()
                    val maxDate = System.currentTimeMillis()
                    monthYearPickerDialog = MonthYearPickerDialogFragment
                        .getInstance(monthSelected, yearSelected, minDate, maxDate, "")
                }
                monthListener =
                    MonthYearPickerDialog.OnDateSetListener { year: Int, monthOfYear: Int ->
                        val calendar: Calendar = Calendar.getInstance()
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, monthOfYear)
                        val sdf = SimpleDateFormat(DateUtils.FORMAT_MONTH, Locale.US)
                        val yearDateFormat = SimpleDateFormat(DateUtils.FORMAT_YEAR, Locale.US)
                        val stringFormat =
                            SimpleDateFormat(DateUtils.FORMAT_STRING_MONTH, Locale.US)
                        currentYear = yearDateFormat.format(calendar.time)
                        currentMonthString = stringFormat.format(calendar.time)
                        currentYearString = calendar.get(Calendar.YEAR).toString()
                        currentMonth = sdf.format(calendar.time)
                        getTransactionsByMonth(currentMonth, currentYear)
                    }
                monthYearPickerDialog.setOnDateSetListener(monthListener)
            })
    }

    override fun onMoneyAdd(moneyModel: MoneyModel) {
        Executors.newSingleThreadExecutor().execute(Runnable {
            passbookViewModel.insertTransaction(moneyModel)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        /*  for (i in 0 until menu.size()) {
              val mi = menu.getItem(i)
              //for aapplying a font to subMenu ...
              val subMenu: SubMenu? = mi.subMenu
              if (subMenu != null && subMenu.size() > 0) {
                  for (j in 0 until subMenu.size()) {
                      val subMenuItem: MenuItem = subMenu.getItem(j)
                      applyFontToMenuItem(subMenuItem)
                  }
              }
              //the method we have create in activity
              applyFontToMenuItem(mi)
          }*/
        return true
    }

    private fun applyFontToMenuItem(mi: MenuItem) {

        val mNewTitle = SpannableString(mi.title)
        mNewTitle.setSpan(
            AppUtils.getInstance(mContext).getAppTypeFace(),
            0,
            mNewTitle.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        mi.title = mNewTitle
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
            R.id.btnEditSalary -> {
                val fragmentTransaction: FragmentTransaction =
                    supportFragmentManager.beginTransaction()
                val prev: Fragment? = supportFragmentManager.findFragmentByTag(this.TAG)
                if (prev != null) {
                    fragmentTransaction.remove(prev)
                }
                fragmentTransaction.addToBackStack(null)
                val dialogEditSalary = DialogEditSalary()
                dialogEditSalary.setContext(this)
                dialogEditSalary.setFrom(Constants.TAG_EDIT)
                dialogEditSalary.show(supportFragmentManager, this.TAG)
                return true
            }
            R.id.btnAddSalary -> {
                val fragmentTransaction: FragmentTransaction =
                    supportFragmentManager.beginTransaction()
                val prev: Fragment? = supportFragmentManager.findFragmentByTag(this.TAG)
                if (prev != null) {
                    fragmentTransaction.remove(prev)
                }
                fragmentTransaction.addToBackStack(null)
                val dialogEditSalary = DialogEditSalary()
                dialogEditSalary.setContext(this)
                dialogEditSalary.setFrom(Constants.TAG_ADD)
                dialogEditSalary.show(supportFragmentManager, this.TAG)
                return true
            }
            R.id.btnDailyExpense -> {
                val fragmentTransaction: FragmentTransaction =
                    supportFragmentManager.beginTransaction()
                val prev: Fragment? = supportFragmentManager.findFragmentByTag(this.TAG)
                if (prev != null) {
                    fragmentTransaction.remove(prev)
                }
                fragmentTransaction.addToBackStack(null)
                val dialogAddExpense = DialogDailyExpense()
                dialogAddExpense.setContext(this)
                dialogAddExpense.setFrom(Constants.TAG_EDIT)
                dialogAddExpense.show(supportFragmentManager, this.TAG)
                return true
            }
            R.id.btnCalendar -> {
//                openDefaultCalendar()
                openMonthAndYearCalendar()
            }
            R.id.btnClear -> {
                Executors.newSingleThreadExecutor().execute(Runnable {
                    passbookViewModel.clearAllTables()
                })
                return true
            }

            R.id.btnPrint -> {
                if (ActivityCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE) != 0) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(WRITE_EXTERNAL_STORAGE),
                        Constants.STORAGE_REQUEST_CODE
                    );
                    return true;
                }
                generatePDF();
                return true;
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onDailyExpenseAdded(expenseList: List<DailyExpenseModel>) {
        var isNoFunds = false
        for (dailyExpenseModel in expenseList) {
            if (!isNoFunds) {
                Executors.newSingleThreadExecutor().execute(
                    Runnable {
                        val value: MoneyModel = passbookViewModel.getLastTransaction()
                        when {
                            value == null -> {
                                isNoFunds = true
                                runOnUiThread(Runnable {
                                    AppUtils.getInstance(mContext)
                                        .makeToast(getString(R.string.insufficient_funds_to_debit))
                                })
                            }
                            value.total <= 0 -> {
                                isNoFunds = true
                                runOnUiThread(Runnable {
                                    AppUtils.getInstance(mContext)
                                        .makeToast(getString(R.string.insufficient_funds_to_debit))
                                })
                            }
                            else -> {
                                var total: Double
                                val debit: Double = dailyExpenseModel.getCost()
                                total = value.total - debit
                                val dateTime: String = DateUtils.getCurrentDateTime()
                                val date: String = DateUtils.getCurrentDate()
                                val month: String = DateUtils.getCurrentMonth()
                                val year: String = DateUtils.getCurrentYear()
                                val time: String = DateUtils.getCurrentTime()
                                val moneyModel = MoneyModel()
                                moneyModel.setDateTime(dateTime)
                                moneyModel.setDate(date)
                                moneyModel.setMonth(month)
                                moneyModel.setYear(year)
                                moneyModel.setTime(time)
                                moneyModel.setParticular(dailyExpenseModel.getExpenseName())
                                moneyModel.credit = 0.0
                                moneyModel.debit = debit
                                moneyModel.total = total
                                passbookViewModel.insertTransaction(moneyModel)
                            }
                        }
                    }
                )
            } else {
                break
            }
        }
    }

    fun onDailyExpenseDeleted(expenseList: List<DailyExpenseModel>) {
        for (dailyExpenseModel in expenseList) {
            Executors.newSingleThreadExecutor().execute(
                Runnable {
                    passbookViewModel.deleteDailyExpense(dailyExpenseModel.get_id())
                }
            )
        }
    }

    private fun openDefaultCalendar() {
        calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val datePickerDialog = DatePickerDialog(
            mContext,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        val datePicker: DatePicker = datePickerDialog.datePicker
        datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun openMonthAndYearCalendar() {
        monthYearPickerDialog.show(supportFragmentManager, null)
    }

    fun generatePDF() {
        Executors.newSingleThreadExecutor().execute(Runnable {
            if (passbookViewModel.getTransactionCount(currentMonth) > 0) {
                val document = PdfDocument()
                val bitmap: Bitmap = getBitmapFromRecyclerView(mainBinding.recyclerView)
                val pdfFile: File = StorageUtils.createPdfFile(currentMonthString)
                try {
                    val outputStream = FileOutputStream(pdfFile)
                    // crate a page description

                    // crate a page description
                    val pageInfo: PdfDocument.PageInfo =
                        PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
                    val page: PdfDocument.Page = document.startPage(pageInfo)
                    bitmap.prepareToDraw()
                    val canvas: Canvas = page.canvas
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, null)
                    document.finishPage(page)
                    document.writeTo(outputStream)
                    document.close()
                    val intent = Intent(mContext, PdfViewActivity::class.java)
                    intent.putExtra(Constants.TAG_FILE_PATH, pdfFile.absolutePath)
                    intent.putExtra(
                        Constants.TAG_MONTH, currentMonthString
                    )
                    intent.putExtra(
                        Constants.TAG_YEAR, currentYearString
                    )
                    startActivity(intent)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun getBitmapFromRecyclerView(recyclerView: RecyclerView): Bitmap {
        val adapter: TranxAdapter = tranxAdapter;
        val size = adapter.itemCount
        var height = 0
        val paint = Paint()
        var iHeight = 0
        val maxMemory: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt();
        // Use 1/8th of the available memory for this memory cache.
        val cacheSize: Int = maxMemory / 8;
        val bitmaCache: LruCache<String, Bitmap> = LruCache(cacheSize)
        var i = 0
        for (i in 0 until size) {
            val holder: RecyclerView.ViewHolder =
                adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
            adapter.onBindViewHolder(holder, i)
            holder.itemView.measure(
                MeasureSpec.makeMeasureSpec(recyclerView.width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            holder.itemView.layout(
                0, 0,
                holder.itemView.measuredWidth,
                holder.itemView.measuredHeight
            )
            holder.itemView.isDrawingCacheEnabled = true
            holder.itemView.buildDrawingCache()
            val drawingCache = holder.itemView.drawingCache
            if (drawingCache != null) {
                bitmaCache.put(i.toString(), drawingCache)
            }
            height += holder.itemView.measuredHeight
        }
        val bigBitmap =
            Bitmap.createBitmap(recyclerView.measuredWidth, height, Bitmap.Config.ARGB_8888)
        val bigCanvas = Canvas(bigBitmap)
        bigCanvas.drawColor(Color.WHITE)
        for (i in 0 until size) {
            val bitmap = bitmaCache[i.toString()]
            bigCanvas.drawBitmap(bitmap, 0f, iHeight.toFloat(), paint)
            iHeight += bitmap.height
            bitmap.recycle()
        }
        return bigBitmap
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.STORAGE_REQUEST_CODE) {
            generatePDF()
        }
    }

}