package it.services.instantpayment.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.services.instantpayment.databinding.PaymentRequestReportItemBinding
import it.services.instantpayment.models.PaymentRequestReportModel

class PaymentRequestReportAdapter(var dataList: ArrayList<PaymentRequestReportModel>) :
    RecyclerView.Adapter<PaymentRequestReportAdapter.ReportViewHolder>() {


    class ReportViewHolder(val binding: PaymentRequestReportItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = PaymentRequestReportItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ReportViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val date = dataList[position].date
        val amount = dataList[position].amount
        val accountNo = dataList[position].accountNo
        val bank = dataList[position].bankName
        val charge = dataList[position].charge
        val txnId = dataList[position].txnId
        val status = dataList[position].status
        val remarks = dataList[position].remarks

        holder.binding.apply {
            tvDate.text = date
            tvAmount.text = "₹ $amount"
            tvAccountNumber.text = accountNo
            tvBank.text = bank
            tvCharge.text = charge
            tvTxnId.text = txnId
            tvStatus.text = status
            tvRemarks.text = remarks
        }
    }

    fun filterData(reportList: java.util.ArrayList<PaymentRequestReportModel>) {
        dataList=reportList
        notifyDataSetChanged()
    }
}