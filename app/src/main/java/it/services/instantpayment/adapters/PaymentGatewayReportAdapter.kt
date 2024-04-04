package it.services.instantpayment.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.services.instantpayment.databinding.PaymentGatewayItemBinding
import it.services.instantpayment.models.GatewayReportModel

class PaymentGatewayReportAdapter(private var dataList: ArrayList<GatewayReportModel>) :
    RecyclerView.Adapter<PaymentGatewayReportAdapter.ReportViewHolder>() {
    class ReportViewHolder(val binding: PaymentGatewayItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding =
            PaymentGatewayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val date = dataList[position].date
        val amount = dataList[position].amount
        val orderId = dataList[position].orderId
        val status = dataList[position].status
        val txnId = dataList[position].txnId
        val panCard = dataList[position].panCard

        holder.binding.apply {
            tvDate.text = date
            tvOrderId.text = orderId
            tvStatus.text = status
            tvTxnId.text = txnId
            tvPanCard.text = panCard
            tvAmount.text = "₹ $amount"
        }
    }

    fun filterData(filteredList: ArrayList<GatewayReportModel>) {
        dataList = filteredList
        notifyDataSetChanged()
    }
}