package it.services.instantpayment.ui.dmt

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentSenderMobileVerificationBinding
import it.services.instantpayment.repository.Response
import it.services.instantpayment.repository.SenderVerificationRepository
import it.services.instantpayment.ui.login.LoginActivity
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.dmt.senderVerification.SenderMobileVerificationViewModel
import it.services.instantpayment.viewModels.dmt.senderVerification.SenderMobileVerificationViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService

class SenderMobileVerificationFragment : Fragment() {

    private lateinit var binding: FragmentSenderMobileVerificationBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var number: String

    private lateinit var senderMobileVerificationViewModel: SenderMobileVerificationViewModel
    private lateinit var progressDialog: AlertDialog
    private lateinit var serviceType: String

    companion object {
        var sType = "DMT1"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSenderMobileVerificationBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()



        serviceType = arguments?.getString("serviceType").toString()

        if (!serviceType.equals("null",true)) {
            binding.rgSType.visibility = View.GONE
        }
        else
        {
            if (!MainActivity.checkPermission("DMT1"))
            {
                binding.rbDmt1.isChecked=false
                binding.rbDmt1.visibility=View.GONE
                if (MainActivity.checkPermission("DMT2"))
                {
                    binding.rbDmt2.isChecked=true
                    binding.rbDmt2.visibility=View.VISIBLE

                }
                else
                {
                    binding.rbDmt2.isChecked=false
                    binding.rbDmt2.visibility=View.GONE
                }
            }
            else
            {
                if (!MainActivity.checkPermission("DMT2"))
                {
                    binding.rbDmt2.isChecked=false
                    binding.rbDmt2.visibility=View.GONE
                }
            }
        }

        progressDialog = CustomDialogs.getCustomProgressDialog(activity)
        handleClickAndEvents()
        setUpViewModels()
        setUpObservers()
        return binding.root
    }

    private fun setUpObservers() {
        senderMobileVerificationViewModel.verifySenderData.observe(viewLifecycleOwner)
        { response ->
            when (response) {
                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    if (response.statusCode.equals("0")) {
                        val bundle = Bundle()
                        bundle.putString("number", number)
                        replaceFragment(AddSenderFragment(), bundle)
                    }
                    Toast.makeText(context, response.errMessage, Toast.LENGTH_LONG).show()
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    val bundle = Bundle()
                    Log.d("data", "setUpObservers: ${response.data}")
                    bundle.putString("response", response.data.toString())
                    bundle.putString("number", number)

                    replaceFragment(BeneListFragment(), bundle)

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
        val repository = SenderVerificationRepository(webService)
        senderMobileVerificationViewModel = ViewModelProvider(
            this,
            SenderMobileVerificationViewModelFactory(repository)
        )[SenderMobileVerificationViewModel::class.java]
    }

    private fun handleClickAndEvents() {
        binding.apply {
            Glide.with(imgLogo).load(LoginActivity.logoImage).into(imgLogo)

            btnValidate.setOnClickListener {
                number = etNumber.text.toString().trim()

                if (number.trim().length == 10) {
                    sType = if (serviceType.equals("UPI", true)) { serviceType }
                    else {
                        if (rbDmt1.isChecked) {
                            "DMT1"
                        }
                        else if (rbDmt2.isChecked) {
                            "DMT2"
                        }
                        else
                        {
                            CustomDialogs.getMessageDialog(
                                activity,
                                "This service is not activated.\nPlease contact admin.",
                                false
                            )
                            return@setOnClickListener
                        }

                    }
                    senderMobileVerificationViewModel.verifySender(
                        MainActivity.LOGIN_SESSION,
                        ApiKeys.SENDER_VERIFY_KEY,
                        number
                    )
                } else {
                    etNumber.requestFocus()
                    etNumber.error = "Invalid"
                }
            }
        }
    }


}