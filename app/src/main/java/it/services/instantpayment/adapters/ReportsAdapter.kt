package it.services.instantpayment.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.services.instantpayment.databinding.DmtReportItemBinding
import it.services.instantpayment.databinding.RechargeReportItemBinding
import it.services.instantpayment.interfaces.AllClickInterface
import org.json.JSONObject
import java.util.PrimitiveIterator

class ReportsAdapter(
    private var reportsList: ArrayList<JSONObject>,
    private val serviceName: String,
    private val allClickInterface: AllClickInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class RechargeViewHolder(val binding: RechargeReportItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class DmtViewHolder(val binding: DmtReportItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun filterData(filterList: ArrayList<JSONObject>) {
        reportsList = filterList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (serviceName.equals("Recharge", true) || serviceName.equals("DTH", true)) {
            val binding = RechargeReportItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return RechargeViewHolder(binding)

        } else if (serviceName.equals("DMT", true) || serviceName.equals("UPI", true)) {
            val binding =
                DmtReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DmtViewHolder(binding)

        } else {

            //FOR BBPS
            val binding = RechargeReportItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return RechargeViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return reportsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (serviceName.equals("Recharge", true) || serviceName.equals("DTH", true)) {

            holder as RechargeViewHolder
            var date = reportsList[position].getString("ReqDate")
            date = date.split("T")[0]

            val amount = reportsList[position].getString("Amount")
            val number = reportsList[position].getString("Mobileno")
            val status = reportsList[position].getString("Status")
            val operator = reportsList[position].getString("OperatorName")

            holder.binding.apply {

                tvDate.text = date
                tvAmount.text = "₹ $amount"
                tvNumber.text = number
                tvStatus.text = status
                tvOperator.text = operator

            }
        }
        else if (serviceName.equals("DMT", true) || serviceName.equals("UPI", true)) {
            holder as DmtViewHolder
            var date = reportsList[position].getString("ReqDate")
            date = date.split("T")[0]

            val amount = reportsList[position].getString("Amount")
            val status = reportsList[position].getString("Status")
            val openingBalance = reportsList[position].getString("OldBal")
            val closingBalance = reportsList[position].getString("NewBal")
            val accountNumber = reportsList[position].getString("AccountNo")
            val bankName = reportsList[position].getString("BankName")
            val txnId = reportsList[position].getString("TxnId")
            val mobileNo = reportsList[position].getString("Mobileno")

            holder.binding.apply {
                tvDate.text = date
                tvTxnId.text = txnId
                tvAmount.text = "₹ $amount"
                tvOpeningBalance.text = "₹ $openingBalance"
                tvClosingBalance.text = "₹ $closingBalance"
                tvStatus.text = status
                tvBankName.text = bankName
                tvAccountNo.text = accountNumber
                tvMobileNo.text = mobileNo

            }
        }
        else {

            // FOR BBPS
            holder as RechargeViewHolder
            var date = reportsList[position].getString("ReqDate")
            date = date.split("T")[0]

            val amount = reportsList[position].getString("Amount")
            val number = reportsList[position].getString("AccountNo")
            val status = reportsList[position].getString("Status")
            val operator = reportsList[position].getString("OperatorName")

            holder.binding.apply {

                tvDate.text = date
                tvAmount.text = "₹ $amount"
                tvNumber.text = number
                tvStatus.text = status
                tvOperator.text = operator
            }
        }

        holder.itemView.setOnClickListener {
            val txnId = reportsList[position].getString("TransId")
            allClickInterface.allClick(reportsList[position],txnId)
        }



    }
}