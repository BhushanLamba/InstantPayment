package it.services.instantpayment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.services.instantpayment.databinding.CreditDebitReportItemBinding
import org.json.JSONArray

class CreditDebitReportAdapter(val dataArray: JSONArray) :
    RecyclerView.Adapter<CreditDebitReportAdapter.CreditDebitViewHolder>() {
    class CreditDebitViewHolder(binding: CreditDebitReportItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditDebitViewHolder {
        val binding =
            CreditDebitReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CreditDebitViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataArray.length()
    }

    override fun onBindViewHolder(holder: CreditDebitViewHolder, position: Int) {
        val date = dataArray.getJSONObject(position).getString("")
    }
}