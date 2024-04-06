package it.services.instantpayment.ui.bbps

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.chaos.view.PinView
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentBbpsBillDetailsBinding
import it.services.instantpayment.repository.BbpsBillPayRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.ui.login.LoginActivity
import it.services.instantpayment.utils.ApiKeys.BBPS_PAY_KEY
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.bbps.BbpsBillPayViewModel
import it.services.instantpayment.viewModels.bbps.BbpsBillPayViewModelFactory
import it.services.instantpayment.viewModels.bbps.BbpsTransactionSlipFragment
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class BbpsBillDetailsFragment : Fragment() {

    private lateinit var binding: FragmentBbpsBillDetailsBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    private lateinit var billAmount: String
    private lateinit var caNumber: String
    private lateinit var dueDate: String
    private lateinit var customerName: String
    private lateinit var provider: String
    private lateinit var operatorId: String
    private lateinit var serviceId: String
    private lateinit var apiResponse: String
    private lateinit var progressDialog: AlertDialog


    private lateinit var bbpsBillPayViewModel: BbpsBillPayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBbpsBillDetailsBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()
        progressDialog = CustomDialogs.getCustomProgressDialog(activity)

        setUpViewModels()
        getSetData()
        handleClickAndEvents()
        setUpObserveres()

        return binding.root
    }

    private fun setUpObserveres() {
        bbpsBillPayViewModel.bbpsBillPayData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    val alertDialog = AlertDialog.Builder(context)
                    alertDialog.setMessage(it.errMessage)
                    alertDialog.setPositiveButton("Ok", null)
                    alertDialog.show()
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    it.data?.let { data ->
                        val bundle = Bundle()
                        bundle.putString("data", data.toString())
                        replaceFragment(BbpsTransactionSlipFragment(), bundle)
                    }
                }

            }
        }
    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle


        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_container, fragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }


    private fun setUpViewModels() {

        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = BbpsBillPayRepository(webService)
        bbpsBillPayViewModel = ViewModelProvider(
            this,
            BbpsBillPayViewModelFactory(repository)
        )[BbpsBillPayViewModel::class.java]
    }

    private fun handleClickAndEvents() {
        binding.apply {
            Glide.with(imgLogo).load(LoginActivity.logoImage).into(imgLogo)

            btnPayBill.setOnClickListener {
                val mpinDialog = CustomDialogs.getMpinDialog(activity)

                val btnCancel: AppCompatButton = mpinDialog.findViewById(R.id.btn_cancel)
                val btnSubmit: AppCompatButton = mpinDialog.findViewById(R.id.btn_submit)
                val mpinView: PinView = mpinDialog.findViewById(R.id.otp_pin_view)



                btnCancel.setOnClickListener { mpinDialog.dismiss() }

                btnSubmit.setOnClickListener {
                    val mpin = mpinView.text.toString().trim()

                    if (mpin.length == 4) {
                        mpinDialog.dismiss()
                        bbpsBillPayViewModel.payBill(
                            MainActivity.LOGIN_SESSION,
                            BBPS_PAY_KEY,
                            serviceId,
                            operatorId,
                            MainActivity.MOBILE_NO,
                            caNumber,
                            billAmount,
                            provider,
                            dueDate,
                            mpin,apiResponse
                        )

                    } else {
                        Toast.makeText(context, "Please enter correct mpin.", Toast.LENGTH_LONG)
                            .show()
                    }
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getSetData() {
        val data = arguments?.getString("data").toString()

        try {
            val responseObject = JSONObject(data)
            val dataArray = responseObject.getJSONArray("Data")
            val dataObject = dataArray.getJSONObject(0)

            operatorId = arguments?.getString("operatorId").toString()
            serviceId = arguments?.getString("serviceId").toString()

            billAmount = dataObject.getString("Billamount")
            caNumber = dataObject.getString("CaNumber")
            dueDate = dataObject.getString("Duedate")
            customerName = dataObject.getString("CustomerName")
            provider = dataObject.getString("Provider")
            apiResponse = dataObject.getString("APIResponse")

            binding.apply {
                tvBillAmount.text = "₹ $billAmount"
                tvCaNumber.text = caNumber
                tvDueDate.text = dueDate
                tvCustomerName.text = customerName


            }

        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }

}