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
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.adapters.ReportsAdapter
import it.services.instantpayment.databinding.FragmentReportsBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.repository.ReportsRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.ApiKeys.RECEIPT_KEY
import it.services.instantpayment.utils.ApiKeys.REPORTS_KEY
import it.services.instantpayment.utils.BottomDateFragment
import it.services.instantpayment.viewModels.reports.ReportViewModelFactory
import it.services.instantpayment.viewModels.reports.ReportsViewModel
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportsFragment : Fragment() {


    private lateinit var binding: FragmentReportsBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    private lateinit var reportsViewModel: ReportsViewModel
    private lateinit var serviceName: String
    private lateinit var fromDate: String
    private lateinit var toDate: String
    private lateinit var apiDateFormat: SimpleDateFormat
    private lateinit var reportList: ArrayList<JSONObject>
    private lateinit var reportsAdapter: ReportsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReportsBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()
        serviceName = arguments?.getString("serviceName", "All").toString()

        apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val calender = Calendar.getInstance()
        val currentYear = calender.get(Calendar.YEAR)
        val currentMonth = calender.get(Calendar.MONTH)
        val today = calender.get(Calendar.DAY_OF_MONTH)

        val fromDateCalender = Calendar.getInstance()
        fromDateCalender.set(currentYear, currentMonth, today)
        fromDate = apiDateFormat.format(fromDateCalender.time)
        toDate = apiDateFormat.format(fromDateCalender.time)

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
                                reportsViewModel.getReports(
                                    MainActivity.LOGIN_SESSION,
                                    REPORTS_KEY,
                                    serviceName,
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
                    if(inputChar.toString().isNotEmpty())
                    {
                        searchData(inputChar.toString())
                    }
                    else
                    {
                        if (::reportList.isInitialized)
                        {
                            reportsAdapter.filterData(reportList)
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
        val filteredList = java.util.ArrayList<JSONObject>()

        if (::reportList.isInitialized)
        {
            for (jsonObject in reportList) {
                if (jsonObject.getString("Mobileno").contains(searchText) ||
                    jsonObject.getString("AccountNo").contains(searchText)
                ) {
                    filteredList.add(jsonObject)
                }
            }

            if (filteredList.isNotEmpty()) {
                reportsAdapter.filterData(filteredList)
            }
            else{
                binding.recycler.visibility=View.GONE
                binding.notFoundLy.visibility=View.VISIBLE
            }

        }
    }

    private fun setUpObserver() {
        reportsViewModel.reportsListData.observe(viewLifecycleOwner)
        { response ->
            when (response) {


                is Response.Success -> {
                    binding.recycler.visibility=View.VISIBLE
                    binding.notFoundLy.visibility=View.GONE

                    reportList = response.data!!
                    reportsAdapter = ReportsAdapter(reportList, serviceName,object : AllClickInterface{
                        override fun allClick(data: Any, txnId: String) {
                            reportsViewModel.getReceipt(MainActivity.LOGIN_SESSION, RECEIPT_KEY,txnId,serviceName)
                        }
                    })
                    binding.apply {
                        recycler.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.recycler.adapter = reportsAdapter
                    }
                }

                else -> { binding.recycler.visibility=View.GONE
                    binding.notFoundLy.visibility=View.VISIBLE
                    binding.recycler.visibility=View.GONE
                }
            }
        }


        reportsViewModel.receiptData.observe(viewLifecycleOwner)
        {
                response ->
            when (response) {


                is Response.Success -> {
                    val dataObject = response.data!!
                    val bundle=Bundle()
                    bundle.putString("dataStr",dataObject.toString())
                    replaceFragment(ReceiptFragment(),bundle)

                }

                else -> {}
            }
        }

    }



    private fun setUpViewModels() {

        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = ReportsRepository(webService)
        reportsViewModel = ViewModelProvider(
            this,
            ReportViewModelFactory(repository)
        )[ReportsViewModel::class.java]

        reportsViewModel.getReports(
            MainActivity.LOGIN_SESSION,
            REPORTS_KEY,
            serviceName,
            fromDate,
            toDate
        )
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