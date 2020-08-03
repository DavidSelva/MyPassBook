package com.david.mypassbook.ui.passbook.dailyexpense

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.david.mypassbook.R
import com.david.mypassbook.databinding.DialogDailyExpenseBinding
import com.david.mypassbook.db.DailyExpenseModel
import com.david.mypassbook.ui.passbook.MainActivity
import com.david.mypassbook.ui.passbook.PassBookViewModel
import com.david.mypassbook.utils.AppUtils
import com.david.mypassbook.utils.Constants
import java.util.concurrent.Executors
import kotlin.jvm.internal.Intrinsics


class DialogDailyExpense : DialogFragment() {
    val TAG = DialogDailyExpense::javaClass.name
    lateinit var binding: DialogDailyExpenseBinding
    private val dailyExpenseModel: DailyExpenseModel? = null
    lateinit var dailyViewModel: PassBookViewModel
    lateinit var expenseAdapter: EditExpenseAdapter
    private val expenseList: List<DailyExpenseModel> = ArrayList()
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
            DialogDailyExpenseBinding.inflate(inflater, container, false)
        this.binding = inflate;
        return inflate.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dailyViewModel = ViewModelProvider(this).get(PassBookViewModel::class.java)
        setAdapter()
        dailyViewModel.dailyExpenses.observe(
            requireActivity(),
            androidx.lifecycle.Observer { dailyList ->
                dailyList.let { expenseAdapter.setData(dailyList) }
            })

        if (mFrom.equals(Constants.TAG_EDIT)) {
            binding.btnDelete.visibility = View.VISIBLE
            binding.editLay.visibility = View.VISIBLE
        } else {
            binding.btnDelete.visibility = View.GONE
            binding.editLay.visibility = View.GONE
        }

        binding.btnAdd.setOnClickListener(View.OnClickListener {
            if (mFrom.equals(Constants.TAG_EDIT)) {
                if (TextUtils.isEmpty(binding.edtExpense.text)) {
                    AppUtils.getInstance(mContext)
                        .makeToast(mContext.getString(R.string.enter_particulars))
                } else {
                    Executors.newSingleThreadExecutor().execute(
                        Runnable {
                            val dailyExpenseModel = DailyExpenseModel()
                            dailyExpenseModel.setExpenseName(binding.edtExpense.text.toString())
                            dailyExpenseModel.setCost(binding.edtCost.text.toString().toDouble())
                            dailyViewModel.insertDailyExpense(dailyExpenseModel)
                        }
                    )
                }
            } else {
                if (expenseAdapter.getCheckedData().isNotEmpty()) {
                    if (activity is MainActivity) {
                        val tempActivity: MainActivity = activity as MainActivity;
                        tempActivity.onDailyExpenseAdded(expenseAdapter.getCheckedData())
                    }
                }
                dismissAllowingStateLoss()
            }
        })
    }

    private fun setAdapter() {
        expenseAdapter = EditExpenseAdapter(mContext, expenseList)
        binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
        expenseAdapter.notifyDataSetChanged()
    }

    fun setContext(mContext: Context) {
        this.mContext = mContext;
    }

    fun setFrom(from: String) {
        this.mFrom = from
    }
}