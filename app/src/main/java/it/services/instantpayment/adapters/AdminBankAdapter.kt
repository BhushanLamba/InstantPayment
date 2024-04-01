package it.services.instantpayment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.services.instantpayment.databinding.BankItemBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.AdminBanksModel
import it.services.instantpayment.models.BankModel

class AdminBankAdapter(
    private var bankList: ArrayList<AdminBanksModel>,
    private val allClickInterface: AllClickInterface
) : RecyclerView.Adapter<AdminBankAdapter.BankViewHolder>() {

    fun filterData(filterList: ArrayList<AdminBanksModel>) {
        bankList = filterList
        notifyDataSetChanged()
    }
    class BankViewHolder(val binding: BankItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val binding = BankItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BankViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bankList.size
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        val bankName = bankList[position].bankName

        holder.binding.apply {
            tvBankName.text = bankName
        }

        holder.itemView.setOnClickListener {
            allClickInterface.allClick(bankList[position], "")
        }
    }

}