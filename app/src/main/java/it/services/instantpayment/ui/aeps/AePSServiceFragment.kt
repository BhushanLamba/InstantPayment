package it.services.instantpayment.ui.aeps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import it.services.instantpayment.MainActivity
import it.services.instantpayment.databinding.FragmentAePSServiceBinding
import it.services.instantpayment.ui.dashboard.HomeFragment
import java.util.Random


class AePSServiceFragment : Fragment() {

    private lateinit var binding: FragmentAePSServiceBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAePSServiceBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()

        binding.apply {
            cashWithdrawalLy.setOnClickListener {
                sendDataToService("1")
            }

            balanceEnquiryLy.setOnClickListener {
                sendDataToService("0")
            }

            miniStatementLy.setOnClickListener {
                sendDataToService("2")
            }

            aadhaarPayLy.setOnClickListener {
                sendDataToService("3")
            }
        }

        return binding.root
    }

    private fun sendDataToService(transactionType: String) {
        val dataModel = HomeFragment.DataModel()
        dataModel.clientRefID = Random().nextInt().toString()
        dataModel.paramB = ""
        dataModel.paramC = ""
        dataModel.applicationType = ""
        dataModel.clientID = "HUZT7jTCzo8VxZwQtivRnN2k0IZKuXIO9vVwmZU9LRYtcppL"
        dataModel.clientSecret = "4A13Ykfae1zIfTS9Ee1R9EbfPwnGHBPtmlHkk0LEZEanM1aTdsb7NtVPt5RS5svA"
        dataModel.userNameFromCoreApp = MainActivity.USERNAME
        dataModel.API_USER_NAME_VALUE = "instpymntapi"
        dataModel.SHOP_NAME = "iServeU"
        dataModel.BRAND_NAME = "Instant Payment"
        dataModel.location = "Infocity,BBSR"
        dataModel.agent = "Instant Retailer"
        dataModel.bankCode = "common"
        dataModel.transactionType = transactionType
        /*dataModel.transactionType = when{
            binding.rbBe.isChecked ->  "0"
            binding.rbCw.isChecked -> "1"
            binding.rbMini.isChecked -> "2"
            binding.rbAdhaarpay.isChecked -> "3"
            binding.rbCd.isChecked -> "4"
            else -> ""
        }*/

        val gson = Gson()
        val getData = gson.toJson(dataModel)
        Log.d("dataModel", "sendDataToService: $getData")
        try {
            val manager = requireActivity().packageManager
            val intent = manager.getLaunchIntentForPackage("com.aeps.aeps_api_user_production")
            intent!!.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.flags = 0
            intent.putExtra("dataToService", getData)
            startActivityForResult(intent, 80)
        } catch (e: Exception) {
            Toast.makeText(context, "Application Not Found", Toast.LENGTH_SHORT).show()
        }
    }

}