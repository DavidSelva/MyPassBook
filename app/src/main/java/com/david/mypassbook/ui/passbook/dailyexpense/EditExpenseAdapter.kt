package com.david.mypassbook.ui.passbook.dailyexpense

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.david.mypassbook.R
import com.david.mypassbook.databinding.ItemDailyExpenseBinding
import com.david.mypassbook.db.DailyExpenseModel


class EditExpenseAdapter internal constructor(
    context: Context,
    dailyList: List<DailyExpenseModel>
) :
    RecyclerView.Adapter<ViewHolder>() {

    private var expenseList: List<DailyExpenseModel> = dailyList
    private var tempList: MutableList<DailyExpenseModel> = ArrayList()
    private val mContext: Context = context

    fun getCheckedData(): List<DailyExpenseModel> {
        return tempList
    }
    fun setData(model: List<DailyExpenseModel>) {
        expenseList = model
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_daily_expense, parent, false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.expenseList.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            val item = expenseList[position]
            holder.viewBinding.edtExpense.text =
                Editable.Factory.getInstance().newEditable(item.getExpenseName())
            holder.viewBinding.edtCost.text =
                Editable.Factory.getInstance().newEditable(item.getCost().toString())
            holder.viewBinding.btnCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    val dailyExpenseModel: DailyExpenseModel = item
                    dailyExpenseModel.setExpenseName(holder.viewBinding.edtExpense.text.toString());
                    dailyExpenseModel.setCost(
                        holder.viewBinding.edtCost.text.toString().toDouble()
                    );
                    tempList.add(dailyExpenseModel)
                } else {
                    tempList.remove(item)
                }
            })
        }
    }

    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        val viewBinding = ItemDailyExpenseBinding.bind(itemView)
    }
}