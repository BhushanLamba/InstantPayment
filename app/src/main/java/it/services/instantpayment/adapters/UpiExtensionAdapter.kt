package it.services.instantpayment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.services.instantpayment.databinding.UpiExtensionItemBinding
import it.services.instantpayment.interfaces.AllClickInterface

class UpiExtensionAdapter(private val dataList: ArrayList<String>,private val allClickInterface: AllClickInterface) :
    RecyclerView.Adapter<UpiExtensionAdapter.UpiExtensionViewHolder>() {



    class UpiExtensionViewHolder(val binding: UpiExtensionItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpiExtensionViewHolder {
        val binding =
            UpiExtensionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpiExtensionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: UpiExtensionViewHolder, position: Int) {
        val extension = dataList[position]
        holder.binding.tvExtension.text = extension
        holder.itemView.setOnClickListener {
            allClickInterface.allClick("",extension)
        }
    }
}