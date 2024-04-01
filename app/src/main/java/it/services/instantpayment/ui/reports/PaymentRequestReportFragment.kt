package it.services.instantpayment.ui.reports

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import it.services.instantpayment.adapters.PaymentRequestReportAdapter
import it.services.instantpayment.databinding.FragmentPaymentRequestReportBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.PaymentRequestReportModel
import it.services.instantpayment.repository.PaymentRequestReportRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.BottomDateFragment
import it.services.instantpayment.viewModels.paymentRequestReport.PaymentRequestReportViewModel
import it.services.instantpayment.viewModels.paymentRequestReport.PaymentRequestReportViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PaymentRequestReportFragment : Fragment() {

    private lateinit var binding: FragmentPaymentRequestReportBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var viewModel: PaymentRequestReportViewModel
    private lateinit var fromDate: String
    private lateinit var toDate: String
    private lateinit var apiDateFormat: SimpleDateFormat
    private lateinit var adapter: PaymentRequestReportAdapter
    private lateinit var reportList: ArrayList<PaymentRequestReportModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentRequestReportBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()

        val calender = Calendar.getInstance()
        val currentYear = calender.get(Calendar.YEAR)
        val currentMonth = calender.get(Calendar.MONTH)
        val today = calender.get(Calendar.DAY_OF_MONTH)


        apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val fromDateCalender = Calendar.getInstance()
        fromDateCalender.set(currentYear, currentMonth, today)
        fromDate = apiDateFormat.format(fromDateCalender.time)
        toDate = apiDateFormat.format(fromDateCalender.time)

        setUpViewModel()
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
                                viewModel.getPaymentRequestReport(
                                    fromDate,
                                    toDate
                                )
                            }
                        })
                bottomDateFragment.show(requireActivity().supportFragmentManager, "")
            }

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(inputChar: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (inputChar.toString().isNotEmpty()) {
                        searchData(inputChar.toString())
                    } else {
                        if (::reportList.isInitialized) {
                            adapter.filterData(reportList)
                            binding.recycler.visibility = View.VISIBLE
                            binding.notFoundLy.visibility = View.GONE
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }



        return binding.root
    }

    private fun setUpObserver() {
        viewModel.reportData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Response.Success -> {

                    reportList = it.data!!
                    binding.recycler.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = PaymentRequestReportAdapter(reportList)
                    binding.recycler.adapter = adapter
                    binding.notFoundLy.visibility = View.GONE
                    binding.recycler.visibility = View.VISIBLE
                }

                else -> {
                    binding.notFoundLy.visibility = View.VISIBLE
                    binding.recycler.visibility = View.GONE

                }
            }
        }


    }

    private fun searchData(searchText: String) {
        val filteredList = ArrayList<PaymentRequestReportModel>()

        if (::reportList.isInitialized) {
            for (model in reportList) {
                if (model.txnId.contains(searchText)
                ) {
                    filteredList.add(model)
                }
            }

            if (filteredList.isNotEmpty()) {
                adapter.filterData(filteredList)
            } else {
                binding.recycler.visibility = View.GONE
                binding.notFoundLy.visibility = View.VISIBLE
            }

        }
    }

    private fun setUpViewModel() {

        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = PaymentRequestReportRepository(webService)
        viewModel = ViewModelProvider(
            this,
            PaymentRequestReportViewModelFactory(repository)
        )[PaymentRequestReportViewModel::class.java]

        viewModel.getPaymentRequestReport(fromDate, toDate)
    }
}