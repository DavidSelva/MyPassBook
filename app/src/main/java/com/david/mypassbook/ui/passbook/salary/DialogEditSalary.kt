package com.david.mypassbook.ui.passbook.salary

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.david.mypassbook.R
import com.david.mypassbook.databinding.DialogEditSalaryBinding
import com.david.mypassbook.db.MoneyModel
import com.david.mypassbook.db.SalaryModel
import com.david.mypassbook.ui.passbook.PassBookViewModel
import com.david.mypassbook.utils.AppUtils
import com.david.mypassbook.utils.Constants
import com.david.mypassbook.utils.DateUtils
import java.util.concurrent.Executors
import kotlin.jvm.internal.Intrinsics


class DialogEditSalary : DialogFragment() {
    val TAG = DialogEditSalary::javaClass.name
    lateinit var binding: DialogEditSalaryBinding
    lateinit var dailyViewModel: PassBookViewModel
    lateinit var mContext: Context
    lateinit var mFrom: String

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val window: Window? = dialog.window
            if (window == null) {
                Intrinsics.throwNpe()
            }
            window?.setLayout(-1, -2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate =
            DialogEditSalaryBinding.inflate(inflater, container, false)
        this.binding = inflate;
        val root = inflate.root
        val itemView: View = root
        dailyViewModel = ViewModelProvider(this).get(PassBookViewModel::class.java)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Executors.newSingleThreadExecutor().execute(Runnable {
            val salaryData: SalaryModel = dailyViewModel.getCurrentSalary()
            if (salaryData != null) {
                val salary = salaryData
                binding.edtAmount.setText(
                    Editable.Factory.getInstance().newEditable(salary.salary.toString())
                )
            }
        })
        when (mFrom) {
            Constants.TAG_ADD -> {
                binding.edtAmount.isEnabled = false
                binding.edtParticulars.isEnabled = false
                binding.btnSave.text = getString(R.string.add)
            }
        }
        binding.btnSave.setOnClickListener(View.OnClickListener {
            if (mFrom == Constants.TAG_EDIT) {
                if (TextUtils.isEmpty(binding.edtAmount.text.toString())) {
                    AppUtils.getInstance(mContext).makeToast(getString(R.string.enter_amount))
                } else {
                    val currentMonth: String = DateUtils.getCurrentMonth()
                    val salaryModel = SalaryModel(
                        currentMonth,
                        binding.edtAmount.text.toString().toDouble()
                    )

                    Executors.newSingleThreadExecutor().execute(Runnable {
                        dailyViewModel.editSalary(salaryModel)
                        dismissAllowingStateLoss()
                    })
                }
            } else {
                if (!TextUtils.isEmpty(binding.edtAmount.text.toString())) {
                    val credit: Double = binding.edtAmount.text.toString().toDouble()
                    val dateTime = DateUtils.getCurrentDateTime()
                    val date = DateUtils.getCurrentDate()
                    val month = DateUtils.getCurrentMonth()
                    val year = DateUtils.getCurrentYear()
                    val time = DateUtils.getCurrentTime()
                    val particulars = binding.edtParticulars.text.toString()
                    val total: Double = credit
                    val moneyModel = MoneyModel()
                    moneyModel.setDateTime(dateTime)
                    moneyModel.setDate(date)
                    moneyModel.setMonth(month)
                    moneyModel.setYear(year)
                    moneyModel.setTime(time)
                    moneyModel.setParticular(particulars)
                    moneyModel.credit = credit
                    moneyModel.debit = 0.0
                    moneyModel.total = total
                    Executors.newSingleThreadExecutor().execute(Runnable {
                        dailyViewModel.insertTransaction(moneyModel)
                        dismissAllowingStateLoss()
                    })
                } else {
                    AppUtils.getInstance(mContext)
                        .makeToast(getString(R.string.edit_salary_description))
                }
            }
        })
    }

    fun setContext(mContext: Context) {
        this.mContext = mContext;
    }

    fun setFrom(from: String) {
        this.mFrom = from
    }
}