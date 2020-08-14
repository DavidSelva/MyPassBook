package com.david.mypassbook.ui.passbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.david.mypassbook.R
import com.david.mypassbook.db.MoneyModel


class TranxAdapter internal constructor(context: Context, dailyList: List<MoneyModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var words = emptyList<MoneyModel>() // Cached copy of words
    private val VIEWTYPE_HEADER = 0
    private val VIEWTYPE_DATA = 1
    private var dailyList: List<MoneyModel> = dailyList
    private val mContext: Context = context

    fun setData(model: List<MoneyModel>) {
        dailyList = model
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == this.VIEWTYPE_HEADER) {
            val itemView = inflater.inflate(R.layout.item_header, parent, false)
            return HeaderViewHolder(itemView)
        } else {
            val itemView = inflater.inflate(R.layout.item_particulars, parent, false)
            return ItemViewHolder(itemView)
        }
    }

    override fun getItemCount(): Int {
        return this.dailyList.size;
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return this.VIEWTYPE_HEADER;
        }
        return this.VIEWTYPE_DATA;
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            val item = dailyList[position]
            holder.txtSerialNo.text = item.get_id().toString()
            holder.txtDate.text = item.getDateTime()
            holder.txtParticulars.text = item.getParticular()
            holder.txtCredit.text = item.credit.toString()
            holder.txtDebit.text = item.debit.toString()
            holder.txtTotal.text = item.total.toString()
        } else if (holder is ItemViewHolder) {
            val item = dailyList[position]
            holder.txtSerialNo.text = item.get_id().toString()
            holder.txtDate.text = item.getDateTime()
            holder.txtParticulars.text = item.getParticular()
            holder.txtCredit.text = item.credit.toString()
            holder.txtDebit.text = item.debit.toString()
            holder.txtTotal.text = item.total.toString()
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSerialNo: TextView = itemView.findViewById(R.id.txtSerialNo)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtParticulars: TextView = itemView.findViewById(R.id.txtParticulars)
        val txtCredit: TextView = itemView.findViewById(R.id.txtCredit)
        val txtDebit: TextView = itemView.findViewById(R.id.txtDebit)
        val txtTotal: TextView = itemView.findViewById(R.id.txtTotal)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSerialNo: TextView = itemView.findViewById(R.id.txtSerialNo)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtParticulars: TextView = itemView.findViewById(R.id.txtParticulars)
        val txtCredit: TextView = itemView.findViewById(R.id.txtCredit)
        val txtDebit: TextView = itemView.findViewById(R.id.txtDebit)
        val txtTotal: TextView = itemView.findViewById(R.id.txtTotal)
    }

}