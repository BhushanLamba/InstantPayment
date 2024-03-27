package it.services.instantpayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import it.services.instantpayment.databinding.OperatorItemBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.OperatorModel

class OperatorAdapter (private val operatorList:ArrayList<OperatorModel>,private val allClickInterface: AllClickInterface) : RecyclerView.Adapter<OperatorAdapter.OperatorViewModel>(){





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperatorViewModel {
        val binding=OperatorItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OperatorViewModel(binding)
    }

    override fun getItemCount(): Int {
       return operatorList.size
    }

    override fun onBindViewHolder(holder: OperatorViewModel, position: Int) {
     holder.binding.apply {
         val operatorName=operatorList[position].operatorName

         tvOperator.text=operatorName
     }

        holder.itemView.setOnClickListener {
            allClickInterface.allClick(operatorList[position],"")
        }
    }

    class OperatorViewModel(val binding:OperatorItemBinding) : RecyclerView.ViewHolder(binding.root)
}