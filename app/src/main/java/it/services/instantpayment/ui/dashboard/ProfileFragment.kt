package it.services.instantpayment.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentProfileBinding
import it.services.instantpayment.ui.changePasswordPin.ChangePasswordPinActivity
import it.services.instantpayment.utils.SharedPref
import org.json.JSONObject

class ProfileFragment : Fragment() {


    private lateinit var binding: FragmentProfileBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var phone:String
    private lateinit var emailId:String
    private lateinit var panCard:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentProfileBinding.inflate(inflater, container, false)
        context=requireContext()
        activity=requireActivity()

        getSetData()

        binding.apply {
            btnChangePassword.setOnClickListener {
                val intent= Intent(context, ChangePasswordPinActivity::class.java)
                intent.putExtra("sType","Password")
                intent.putExtra("isForget",false)
                intent.putExtra("phone",phone)
                intent.putExtra("email",emailId)
                intent.putExtra("panCard",panCard)
                startActivity(intent)
            }

            btnChangeMpin.setOnClickListener {
                val intent= Intent(context, ChangePasswordPinActivity::class.java)
                intent.putExtra("sType","MPIN")
                intent.putExtra("phone",phone)
                intent.putExtra("email",emailId)
                intent.putExtra("panCard",panCard)
                intent.putExtra("isForget",false)
                startActivity(intent)
            }
        }

        return binding.root
    }

    private fun getSetData() {
        val loginData= SharedPref.getString(context,SharedPref.LOGIN_DATA_KEY).toString()
        try {
            val loginObject= JSONObject(loginData)
            val dataArray=loginObject.getJSONArray("Data")
            val dataObject=dataArray.getJSONObject(0)

            val name=dataObject.getString("Name")
            val firmName=dataObject.getString("CompanyName")
            emailId=dataObject.getString("EmailId")
            phone=dataObject.getString("Phone")
            panCard=dataObject.getString("PanCard")

            binding.apply {
                tvName.text=name
                tvFirmName.text=firmName
                tvEmail.text=emailId
                tvPhone.text=phone
                tvPan.text=panCard
            }

        }
        catch (ignore:Exception)
        {

        }
    }

}