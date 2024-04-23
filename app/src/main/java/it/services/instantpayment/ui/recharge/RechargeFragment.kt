package it.services.instantpayment.ui.recharge

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.chaos.view.PinView
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentRechargeBinding
import it.services.instantpayment.repository.BalanceRepository
import it.services.instantpayment.repository.RechargeRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.utils.ApiKeys.Recharge_KEY
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.balance.BalanceViewModel
import it.services.instantpayment.viewModels.balance.BalanceViewModelFactory
import it.services.instantpayment.viewModels.recharge.RechargeViewModel
import it.services.instantpayment.viewModels.recharge.RechargeViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService

class RechargeFragment : Fragment() {

    private lateinit var binding: FragmentRechargeBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var number: String
    private lateinit var amount: String

    private lateinit var rechargeViewModel: RechargeViewModel
    private lateinit var balanceViewModel: BalanceViewModel

    private lateinit var progressDialog: AlertDialog

    private lateinit var operatorName: String
    private lateinit var operatorId: String
    private lateinit var serviceId: String
    private lateinit var operatorImage: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRechargeBinding.inflate(inflater, container, false)
        context = requireContext();
        activity = requireActivity()
        progressDialog = CustomDialogs.getCustomProgressDialog(activity)

        setUpViewModel()
        setUpObservers()

        getSetData()
        handleClicksAndEvents()

        return binding.root

    }

    @SuppressLint("SetTextI18n")
    private fun setUpObservers() {
        rechargeViewModel.rechargeData.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    //Toast.makeText(context, it.errMessage, Toast.LENGTH_LONG).show()
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
                        replaceFragment(RechargeTransactionSlipFragment(), bundle)
                    }
                }

            }
        }
        balanceViewModel.balanceData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Response.Loading -> {
                }

                is Response.Error -> {
                    binding.tvBalance.text = "Wallet Balance Available :\n₹ 00.0"
                }

                is Response.Success -> {
                    it.data?.let { data ->
                        binding.tvBalance.text = "Wallet Balance Available :\n₹ $data"
                    }
                }
            }
        }
    }

    private fun setUpViewModel() {
        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = RechargeRepository(webService)
        val balanceRepository = BalanceRepository(webService)


        rechargeViewModel = ViewModelProvider(
            this,
            RechargeViewModelFactory(repository)
        )[RechargeViewModel::class.java]

        balanceViewModel = ViewModelProvider(
            this,
            BalanceViewModelFactory(balanceRepository)
        )[BalanceViewModel::class.java]
        balanceViewModel.getBalance(MainActivity.LOGIN_SESSION, ApiKeys.BALANCE_KEY)
    }

    @SuppressLint("SetTextI18n")
    private fun handleClicksAndEvents() {
        binding.apply {

            tvProceed.setOnClickListener {
                number = etNumber.text.toString().trim()
                amount = etAmount.text.toString().trim()

                if (number.length != 10) {
                    etNumber.requestFocus()
                    etNumber.error = "Invalid"
                } else if (amount.equals("", true)) {
                    etAmount.requestFocus()
                    etAmount.error = "Required"
                } else {
                    val mpinDialog = CustomDialogs.getMpinDialog(activity)

                    val btnCancel: AppCompatButton = mpinDialog.findViewById(R.id.btn_cancel)
                    val btnSubmit: AppCompatButton = mpinDialog.findViewById(R.id.btn_submit)
                    val mpinView: PinView = mpinDialog.findViewById(R.id.otp_pin_view)
                    val tvInfo: TextView = mpinDialog.findViewById(R.id.tv_info)

                    tvInfo.text =
                        "Operator  :  $operatorName\nNumber  :  $number\nAmount  :  ₹ $amount"

                    btnCancel.setOnClickListener { mpinDialog.dismiss() }

                    btnSubmit.setOnClickListener {
                        val mpin = mpinView.text.toString().trim()

                        if (mpin.length == 4) {
                            mpinDialog.dismiss()
                            rechargeViewModel.doRecharge(
                                MainActivity.LOGIN_SESSION,
                                Recharge_KEY,
                                serviceId,
                                operatorId,
                                number,
                                amount,
                                mpin
                            )

                        } else {
                            Toast.makeText(context, "Please enter correct mpin.", Toast.LENGTH_LONG)
                                .show()
                        }
                    }


                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getSetData() {
        operatorImage = arguments?.getString("operatorImage").toString()
        Glide.with(binding.imgOperator).load(operatorImage).into(binding.imgOperator)

        operatorName = arguments?.getString("operatorName").toString()
        operatorId = arguments?.getString("operatorId").toString()
        serviceId = arguments?.getString("serviceId").toString()

        binding.tvOperator.text = operatorName
        binding.tvCompanyName.text = "${MainActivity.COMPANY_NAME} Wallet"
    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_container, fragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }

}