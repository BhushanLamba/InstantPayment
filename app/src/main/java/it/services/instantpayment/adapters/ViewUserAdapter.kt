package it.services.instantpayment.adapters

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.services.instantpayment.databinding.ViewUserItemBinding
import it.services.instantpayment.interfaces.AllClickInterface
import org.json.JSONArray
import org.json.JSONObject

class ViewUserAdapter(val dataArray: JSONArray, val allClickInterface: AllClickInterface) :
    RecyclerView.Adapter<ViewUserAdapter.ViewUserViewHolder>() {

    class ViewUserViewHolder(val binding: ViewUserItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewUserViewHolder {
        val binding =
            ViewUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewUserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataArray.length()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewUserViewHolder, position: Int) {

        val userName = dataArray.getJSONObject(position).getString("Name")
        val mobile = dataArray.getJSONObject(position).getString("Phone")
        val balance = dataArray.getJSONObject(position).getString("MainBalance")
        val userType = dataArray.getJSONObject(position).getString("Usertype")


        holder.binding.apply {
            tvUserName.text = userName
            tvMobile.text = mobile
            tvBalance.text = "₹ $balance"
            tvUserType.text = userType

            tvCredit.setOnClickListener {

                if (!TextUtils.isEmpty(etAmount.text.toString().trim())) {
                    val amount = etAmount.text.toString().trim()
                    val transferUserId = dataArray.getJSONObject(position).getString("Id")
                    val transferObject = JSONObject()
                    transferObject.put("transferUserId", transferUserId)
                    transferObject.put("transactionType", "Credit")
                    transferObject.put("amount", amount)
                    allClickInterface.allClick(transferObject, "")
                } else {
                    etAmount.error = "Required"
                    etAmount.requestFocus()
                }


            }

            tvDebit.setOnClickListener {

                if (!TextUtils.isEmpty(etAmount.text.toString().trim())) {
                    val amount = etAmount.text.toString().trim()
                    val transferUserId = dataArray.getJSONObject(position).getString("Id")
                    val transferObject = JSONObject()
                    transferObject.put("transferUserId", transferUserId)
                    transferObject.put("transactionType", "Debit")
                    transferObject.put("amount", amount)
                    allClickInterface.allClick(transferObject, "")
                } else {
                    etAmount.error = "Required"
                    etAmount.requestFocus()
                }


            }

        }
    }
}