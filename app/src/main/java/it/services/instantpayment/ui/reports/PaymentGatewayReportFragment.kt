package it.services.instantpayment.ui.reports

import android.annotation.SuppressLint
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
import it.services.instantpayment.adapters.PaymentGatewayReportAdapter
import it.services.instantpayment.databinding.FragmentPaymentGatewayReportBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.GatewayReportModel
import it.services.instantpayment.repository.PaymentGatewayReportRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.BottomDateFragment
import it.services.instantpayment.viewModels.paymentGatewayReport.PaymentGatewayReportViewModel
import it.services.instantpayment.viewModels.paymentGatewayReport.PaymentGatewayReportViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PaymentGatewayReportFragment : Fragment() {

    private lateinit var binding: FragmentPaymentGatewayReportBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var viewModel: PaymentGatewayReportViewModel
    private lateinit var serviceType: String
    private lateinit var fromDate: String
    private lateinit var toDate: String
    private lateinit var apiDateFormat: SimpleDateFormat
    private lateinit var dataList: ArrayList<GatewayReportModel>
    private lateinit var adapter: PaymentGatewayReportAdapter


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentGatewayReportBinding.inflate(inflater, container, false)
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
        serviceType = arguments?.getString("service").toString()

        if (serviceType.equals("UPI",true))
        {
            binding.tvTitle.text="UPI Gateway"
        }

        setUpViewModels()
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
                                viewModel.getGatewayReport(
                                    fromDate,
                                    toDate,
                                    serviceType
                                )
                            }
                        })
                bottomDateFragment.show(requireActivity().supportFragmentManager, "")
            }

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(inputChar: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(inputChar.toString().isNotEmpty())
                    {
                        searchData(inputChar.toString())
                    }
                    else
                    {
                        if (::dataList.isInitialized)
                        {
                            adapter.filterData(dataList)
                            binding.recycler.visibility=View.VISIBLE
                            binding.notFoundLy.visibility=View.GONE
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }

        return binding.root
    }


    private fun searchData(searchText: String) {
        val filteredList = ArrayList<GatewayReportModel>()

        if (::dataList.isInitialized)
        {
            for (model in dataList) {
                if (model.orderId.lowercase().contains(searchText) || model.txnId.lowercase().contains(searchText)|| model.panCard.lowercase().contains(searchText)
                ) {
                    filteredList.add(model)
                }
            }

            if (filteredList.isNotEmpty()) {
                adapter.filterData(filteredList)
            }
            else{
                binding.recycler.visibility=View.GONE
                binding.notFoundLy.visibility=View.VISIBLE
            }

        }
    }

    private fun setUpObserver() {
        viewModel.reportData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Response.Success -> {
                    dataList = it.data!!
                    adapter = PaymentGatewayReportAdapter(dataList)
                    binding.apply {
                        recycler.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        recycler.adapter = adapter

                        recycler.visibility = View.VISIBLE
                        notFoundLy.visibility = View.GONE
                    }
                }

                else -> {
                    binding.recycler.visibility = View.GONE
                    binding.notFoundLy.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpViewModels() {
        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = PaymentGatewayReportRepository(webService)

        viewModel = ViewModelProvider(
            this,
            PaymentGatewayReportViewModelFactory(repository)
        )[PaymentGatewayReportViewModel::class.java]

        viewModel.getGatewayReport("2024-01-01", "2024-04-01", serviceType)
    }

}