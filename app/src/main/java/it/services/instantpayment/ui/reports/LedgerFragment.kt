package it.services.instantpayment.ui.reports

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import it.services.instantpayment.R
import it.services.instantpayment.adapters.LedgerAdapter
import it.services.instantpayment.databinding.FragmentLedgerBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.LedgerModel
import it.services.instantpayment.repository.LedgerRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.BottomDateFragment
import it.services.instantpayment.viewModels.ledger.LedgerViewModel
import it.services.instantpayment.viewModels.ledger.LedgerViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LedgerFragment : Fragment() {

    private lateinit var binding:FragmentLedgerBinding
    private lateinit var context:Context
    private lateinit var activity:Activity
    private lateinit var viewModel: LedgerViewModel
    private lateinit var serviceType:String
    private lateinit var dataList:ArrayList<LedgerModel>
    private lateinit var adapter:LedgerAdapter
    private lateinit var fromDate: String
    private lateinit var toDate: String
    private lateinit var apiDateFormat: SimpleDateFormat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentLedgerBinding.inflate(inflater, container, false)
        context=requireContext()
        activity=requireActivity()

        val calender = Calendar.getInstance()
        val currentYear = calender.get(Calendar.YEAR)
        val currentMonth = calender.get(Calendar.MONTH)
        val today = calender.get(Calendar.DAY_OF_MONTH)


        apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val fromDateCalender = Calendar.getInstance()
        fromDateCalender.set(currentYear, currentMonth, today)
        fromDate = apiDateFormat.format(fromDateCalender.time)
        toDate = apiDateFormat.format(fromDateCalender.time)

        serviceType=arguments?.getString("service").toString()
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
                                viewModel.getLedger(
                                    fromDate,
                                    toDate,
                                    serviceType
                                )
                            }
                        })
                bottomDateFragment.show(requireActivity().supportFragmentManager, "")
            }
        }


        return binding.root
    }

    private fun setUpObserver() {
        viewModel.reportData.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is Response.Success->
                {
                    dataList=it.data!!
                    adapter=LedgerAdapter(dataList)
                    binding.apply {
                        recycler.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                        recycler.adapter=adapter

                        recycler.visibility=View.VISIBLE
                        notFoundLy.visibility=View.GONE
                    }
                }
                else->
                {
                    binding.recycler.visibility=View.GONE
                    binding.notFoundLy.visibility=View.VISIBLE
                }
            }
        }
    }

    private fun setUpViewModel() {

        val webService=RetrofitClient.getInstance().create(WebService::class.java)
        val repository=LedgerRepository(webService)
        viewModel=ViewModelProvider(this, LedgerViewModelFactory(repository))[LedgerViewModel::class.java]

        viewModel.getLedger(fromDate,toDate,serviceType)
    }
}