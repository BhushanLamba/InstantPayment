package it.services.instantpayment.ui.dashboard

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentProfileBinding
import it.services.instantpayment.utils.SharedPref
import org.json.JSONObject

class ProfileFragment : Fragment() {


    private lateinit var binding: FragmentProfileBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentProfileBinding.inflate(inflater, container, false)
        context=requireContext()
        activity=requireActivity()

        getSetData()

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
            val emailId=dataObject.getString("EmailId")
            val phone=dataObject.getString("Phone")
            val panCard=dataObject.getString("PanCard")

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