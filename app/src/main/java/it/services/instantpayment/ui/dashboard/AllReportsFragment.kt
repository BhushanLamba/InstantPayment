package it.services.instantpayment.ui.dashboard

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentReportsAllBinding
import it.services.instantpayment.ui.reports.LedgerFragment
import it.services.instantpayment.ui.reports.PaymentGatewayReportFragment
import it.services.instantpayment.ui.reports.PaymentRequestReportFragment
import it.services.instantpayment.ui.reports.ReportsFragment

class AllReportsFragment : Fragment() {

    private lateinit var binding: FragmentReportsAllBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportsAllBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()

        handleClicks()

        return binding.root
    }

    private fun handleClicks() {
        binding.apply {
            prepaidReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "RECHARGE")
                replaceFragment(ReportsFragment(), bundle)
            }

            dthReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "DTH")
                replaceFragment(ReportsFragment(), bundle)
            }

            dmtReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "DMT")
                replaceFragment(ReportsFragment(), bundle)
            }

            upiDmtReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "UPI")
                replaceFragment(ReportsFragment(), bundle)
            }

            aepsReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "AEPS")
                replaceFragment(ReportsFragment(), bundle)
            }

            electricityReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "ELECTRICITY")
                replaceFragment(ReportsFragment(), bundle)
            }

            fastagReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "FasTag")
                replaceFragment(ReportsFragment(), bundle)
            }

            gasReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "GAS")
                replaceFragment(ReportsFragment(), bundle)
            }

            loanReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "LOAN")
                replaceFragment(ReportsFragment(), bundle)
            }

            postpaidReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "POSTPAID")
                replaceFragment(ReportsFragment(), bundle)
            }

            creditCardReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "CREDIT")
                replaceFragment(ReportsFragment(), bundle)
            }

            postpaidReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "POSTPAID")
                replaceFragment(ReportsFragment(), bundle)
            }

            licReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("serviceName", "LIC")
                replaceFragment(ReportsFragment(), bundle)
            }

            paymentRequestReportLy.setOnClickListener {
                replaceFragment(PaymentRequestReportFragment(),Bundle())
            }

            razorPayReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("service", "Razorpay")
                replaceFragment(PaymentGatewayReportFragment(), bundle)
            }

            upiReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("service", "UPI")
                replaceFragment(PaymentGatewayReportFragment(), bundle)
            }

            ledgerLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("service", "LR")
                replaceFragment(LedgerFragment(), bundle)
            }

            wtrReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("service", "WTR")
                replaceFragment(LedgerFragment(), bundle)
            }
            ptrReportLy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("service", "PTR")
                replaceFragment(LedgerFragment(), bundle)
            }

        }
    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        val fragmentManager = getActivity()?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frame_container, fragment)
        fragmentTransaction?.addToBackStack("")
        fragmentTransaction?.commit()
    }


}