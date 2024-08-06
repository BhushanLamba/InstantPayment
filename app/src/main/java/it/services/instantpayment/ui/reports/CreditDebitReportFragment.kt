package it.services.instantpayment.ui.reports

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.MainActivity
import it.services.instantpayment.databinding.FragmentCreditDebitBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.repository.CreditDebitReportRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.BottomDateFragment
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.creditDebitReport.CreditDebitReportViewModel
import it.services.instantpayment.viewModels.creditDebitReport.CreditDebitReportViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CreditDebitReportFragment : Fragment() {

    private lateinit var binding: FragmentCreditDebitBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var fromDate: String
    private lateinit var toDate: String
    private lateinit var apiDateFormat: SimpleDateFormat
    private lateinit var serviceType: String
    private lateinit var creditDebitReportViewModel: CreditDebitReportViewModel
    private lateinit var progressDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreditDebitBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()

        progressDialog=CustomDialogs.getCustomProgressDialog(activity)

        val calender = Calendar.getInstance()
        val currentYear = calender.get(Calendar.YEAR)
        val currentMonth = calender.get(Calendar.MONTH)
        val today = calender.get(Calendar.DAY_OF_MONTH)

        apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val fromDateCalender = Calendar.getInstance()
        fromDateCalender.set(currentYear, currentMonth, today)
        fromDate = apiDateFormat.format(fromDateCalender.time)
        toDate = apiDateFormat.format(fromDateCalender.time)

        serviceType = arguments?.getString("service").toString()
        serUpViewModel()
        setUpObserver()


        binding.apply {
            filterLy.setOnClickListener {
                val bottomDateFragment =
                    BottomDateFragment(
                        context,
                        activity,
                        fromDate,
                        toDate,
                        object : AllClickInterface {
                            override fun allClick(data: Any, extra: String) {
                                fromDate = data.toString()
                                toDate = extra
                                creditDebitReportViewModel.getCreditDebitReport(MainActivity.LOGIN_SESSION,"Report001",
                                    fromDate,toDate,serviceType,MainActivity.USER_TYPE)
                            }
                        })
                bottomDateFragment.show(requireActivity().supportFragmentManager, "")
            }
        }


        return binding.root
    }

    private fun setUpObserver() {
        creditDebitReportViewModel.creditDebitReportData.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is Response.Error -> {
                    progressDialog.dismiss()
                }
                is Response.Loading -> {
                    progressDialog.show()
                }
                is Response.Success -> {
                    progressDialog.dismiss()
                }
            }
        }
    }

    private fun serUpViewModel() {

        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = CreditDebitReportRepository(webService)

        creditDebitReportViewModel = ViewModelProvider(
            this,
            CreditDebitReportViewModelFactory(repository)
        )[CreditDebitReportViewModel::class.java]

        creditDebitReportViewModel.getCreditDebitReport(MainActivity.LOGIN_SESSION,"Report001",
            fromDate,toDate,serviceType,MainActivity.USER_TYPE)
    }
}