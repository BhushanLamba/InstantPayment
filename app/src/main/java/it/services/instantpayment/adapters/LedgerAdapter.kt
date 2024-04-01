package it.services.instantpayment.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.services.instantpayment.databinding.LedgerItemBinding
import it.services.instantpayment.models.LedgerModel

class LedgerAdapter(private var dataList: ArrayList<LedgerModel>) :
    RecyclerView.Adapter<LedgerAdapter.LedgerViewHolder>() {


    class LedgerViewHolder(val binding: LedgerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LedgerViewHolder {
        val binding = LedgerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LedgerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LedgerViewHolder, position: Int) {
        val date = dataList[position].date
        val amount = dataList[position].amount
        val oldBalance = dataList[position].oldBalance
        val newBalance = dataList[position].newBalance
        val txnType = dataList[position].txnType
        val crDrType = dataList[position].crDrType
        val remarks = dataList[position].remarks


        holder.binding.apply {
            tvDate.text = date
            tvAmount.text = "₹ $amount"
            tvOldBalance.text = "₹ $oldBalance"
            tvNewBalance.text = "₹ $newBalance"
            tvTxnType.text = txnType
            tvCrDr.text = crDrType
            tvRemarks.text = remarks
        }
    }
}