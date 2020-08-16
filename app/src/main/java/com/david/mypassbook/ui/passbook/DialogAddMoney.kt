package com.david.mypassbook.ui.passbook

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.david.mypassbook.R
import com.david.mypassbook.callbacks.DialogMoneyCallback
import com.david.mypassbook.databinding.DialogAddMoneyBinding
import com.david.mypassbook.db.MoneyModel
import com.david.mypassbook.db.MyPassBookDao
import com.david.mypassbook.db.MyPassBookDatabase
import com.david.mypassbook.utils.AppUtils
import com.david.mypassbook.utils.DateUtils
import java.util.concurrent.Executors
import kotlin.jvm.internal.Intrinsics


class DialogAddMoney : DialogFragment() {
    val TAG = DialogAddMoney::javaClass.name
    lateinit var binding: DialogAddMoneyBinding
    lateinit var callback: DialogMoneyCallback
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
            DialogAddMoneyBinding.inflate(inflater, container, false)
        this.binding = inflate;
        return inflate.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.txtDate.text = DateUtils.getCurrentDateTime()
        val passbookDao: MyPassBookDao = MyPassBookDatabase.getDatabase(mContext).myPassBookDao()
        binding.btnAdd.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(binding.edtParticulars.text.toString().trim())) {
                AppUtils.getInstance(mContext).makeToast(getString(R.string.enter_particulars))
            } else if (TextUtils.isEmpty(binding.edtAmount.text.toString().trim())) {
                AppUtils.getInstance(mContext).makeToast(getString(R.string.enter_amount))
            } else if (!binding.btnCredit.isChecked && !binding.btnDebit.isChecked) {
                AppUtils.getInstance(mContext).makeToast(getString(R.string.select_debit_or_credit))
            } else {
                val credit: Double
                val debit: Double
                var total: Double = 0.0
                val dateTime = DateUtils.getCurrentDateTime()
                val date = DateUtils.getCurrentDate()
                val month = DateUtils.getCurrentMonth()
                val year = DateUtils.getCurrentYear()
                val time = DateUtils.getCurrentTime()
                val particulars: String = binding.edtParticulars.text.toString()
                var isCredit: Boolean = false
                if (binding.btnCredit.isChecked) {
                    credit = binding.edtAmount.text.toString().toDouble();
                    debit = java.lang.Double.valueOf(0.0)
                    isCredit = true
                } else {
                    debit = binding.edtAmount.text.toString().toDouble();
                    credit = java.lang.Double.valueOf(0.0)
                    isCredit = false
                }
                Executors.newSingleThreadExecutor().execute(Runnable {
                    val lastData = passbookDao.getLastTransaction()
                    if (!isCredit && lastData == null) {
                        requireActivity().runOnUiThread(Runnable {
                            AppUtils.getInstance(mContext)
                                .makeToast(getString(R.string.insufficient_funds_to_debit))
                        })
                    } else if (!isCredit) {
                        if (debit > 0 && lastData.total <= 0) {
                            requireActivity().runOnUiThread(Runnable {
                                AppUtils.getInstance(mContext)
                                    .makeToast(getString(R.string.insufficient_funds_to_debit))
                            })
                        } else if (debit > 0 && lastData.total < debit) {
                            requireActivity().runOnUiThread(Runnable {
                                AppUtils.getInstance(mContext)
                                    .makeToast(getString(R.string.insufficient_funds_to_debit))
                            })
                        } else {
                            val moneyModel = getModel(
                                particulars,
                                credit,
                                debit,
                                isCredit,
                                lastData.total,
                                dateTime,
                                date,
                                time,
                                month,
                                year
                            )
                            callback.onMoneyAdd(moneyModel)
                            dismissAllowingStateLoss()
                        }

                    } else {
                        val moneyModel = getModel(
                            particulars,
                            credit,
                            debit,
                            isCredit,
                            if (lastData != null) lastData.total else 0.0,
                            dateTime,
                            date,
                            time,
                            month,
                            year
                        )
                        callback.onMoneyAdd(moneyModel)
                        dismissAllowingStateLoss()
                    }
                })
            }
        })
    }

    private fun getModel(
        particulars: String,
        credit: Double,
        debit: Double,
        isCredit: Boolean,
        total: Double,
        dateTime: String,
        date: String,
        time: String,
        month: String,
        year: String
    ): MoneyModel {
        var tempTotal = total
        val moneyModel = MoneyModel()
        moneyModel.setParticular(particulars)
        moneyModel.credit = credit
        moneyModel.debit = debit
        if (isCredit) {
            tempTotal += credit
        } else {
            tempTotal -= debit
        }
        moneyModel.total = tempTotal
        moneyModel.setDateTime(dateTime)
        moneyModel.setDate(date)
        moneyModel.setMonth(month)
        moneyModel.setYear(year)
        moneyModel.setTime(time)
        return moneyModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        callback = context as DialogMoneyCallback
    }
}