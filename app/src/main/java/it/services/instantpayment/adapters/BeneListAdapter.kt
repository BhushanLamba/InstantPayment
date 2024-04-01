package it.services.instantpayment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import it.services.instantpayment.databinding.BeneItemBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.BeneListModel

class BeneListAdapter(private var dataList:ArrayList<BeneListModel>,private val allClickInterface: AllClickInterface) : RecyclerView.Adapter<BeneListAdapter.BeneListViewHolder>() {


    class BeneListViewHolder(val binding: BeneItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeneListViewHolder {
       val binding=BeneItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BeneListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: BeneListViewHolder, position: Int) {

        val beneName=dataList[position].beneName
        val accountNumber=dataList[position].accountNumber
        val ifsc=dataList[position].ifsc
        val bankName=dataList[position].bankName

        holder.binding.apply {
            tvBeneName.text=beneName
            tvAccountNo.text=accountNumber
            tvIfsc.text=ifsc
            tvBankName.text=bankName

            tvPay.setOnClickListener {
                val amount=etAmount.text.toString().trim()
                if (amount.equals("",true))
                {
                    etAmount.error="Invalid"
                }
                else
                {
                    allClickInterface.allClick(dataList[position],amount)
                }
            }

        }


    }

    fun filterData(filteredList: ArrayList<BeneListModel>) {
        dataList=filteredList
        notifyDataSetChanged()
    }
}