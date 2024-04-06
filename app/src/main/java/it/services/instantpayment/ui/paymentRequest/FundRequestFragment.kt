package it.services.instantpayment.ui.paymentRequest

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentFundRequestBinding
import it.services.instantpayment.models.AdminBanksModel
import it.services.instantpayment.repository.PaymentRequestRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.ui.login.LoginActivity
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.paymentRequest.PaymentRequestViewModel
import it.services.instantpayment.viewModels.paymentRequest.PaymentRequestViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService


class FundRequestFragment : Fragment() {

    private lateinit var binding: FragmentFundRequestBinding
    private lateinit var paymentRequestViewModel: PaymentRequestViewModel
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var progressDialog: AlertDialog

    private lateinit var bankList: ArrayList<AdminBanksModel>
    private lateinit var bankId: String
    private lateinit var bankName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFundRequestBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()
        progressDialog = CustomDialogs.getCustomProgressDialog(activity)
        setUpViewModels()
        setUpObservers()
        handleClicksAndEvents()


        return binding.root
    }

    private fun handleClicksAndEvents() {
        binding.apply {
            Glide.with(imgLogo).load(LoginActivity.logoImage).into(imgLogo)

            etBank.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("bankList", bankList)
                replaceFragment(AdminBankFragment(), bundle)
            }

            btnProceed.setOnClickListener {


                val amount = etAmount.text.toString().trim()
                val txnId = etTxnId.text.toString().trim()


                if (amount.equals("", true)) {
                    etAmount.error = "Required"
                    return@setOnClickListener
                }
                if (txnId.equals("", true)) {
                    etTxnId.error = "Required"
                    return@setOnClickListener
                }
                val depositMode = if (rbOnline.isChecked) {
                    "ONLINE"
                } else if (rbCash.isChecked) {
                    "CASH"
                } else {
                    "OFFLINE"
                }

                paymentRequestViewModel.doPaymentRequest(
                    bankId,
                    amount,
                    bankName,
                    txnId,
                    "NA",
                    depositMode
                )
            }
        }
    }

    private fun setUpObservers() {
        paymentRequestViewModel.adminBankData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Response.Error -> {
                    progressDialog.dismiss()
                    getActivity()?.supportFragmentManager?.popBackStackImmediate()
                }

                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Success -> {
                    bankList = it.data!!
                    progressDialog.dismiss()
                }
            }
        }

        paymentRequestViewModel.paymentRequestData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(context, it.errMessage, Toast.LENGTH_LONG).show()
                }

                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Success -> {
                    AlertDialog.Builder(context)
                        .setMessage("Payment Request done successfully.")
                        .setPositiveButton(
                            "OK"
                        ) { _, _ ->
                            getActivity()?.supportFragmentManager?.popBackStackImmediate()
                        }
                        .setCancelable(false)
                        .show()
                    progressDialog.dismiss()
                }
            }
        }
    }

    private fun setUpViewModels() {

        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = PaymentRequestRepository(webService)
        paymentRequestViewModel = ViewModelProvider(
            this,
            PaymentRequestViewModelFactory(repository)
        )[PaymentRequestViewModel::class.java]
        paymentRequestViewModel.getAdminBanks()
    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle

        if (fragment is AdminBankFragment) {
            fragment.bankData.observe(viewLifecycleOwner)
            { bankModel ->

                bankName = bankModel.bankName
                val ifsc = bankModel.ifsc
                val accountNumber = bankModel.accountNo
                val charges = bankModel.charges
                binding.etBank.setText(bankName)
                binding.etIfsc.setText(ifsc)
                binding.etAccountNo.setText(accountNumber)
                binding.etCharges.setText(charges)
                bankId = bankModel.bankId
                binding.detailsLy.visibility = View.VISIBLE

            }
        }

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_container, fragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }
}