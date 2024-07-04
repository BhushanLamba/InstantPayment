package it.services.instantpayment.ui.bbps

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentBbpsBillFetchBinding
import it.services.instantpayment.models.OperatorModel
import it.services.instantpayment.repository.BbpsBillFetchRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.ui.login.LoginActivity
import it.services.instantpayment.utils.ApiKeys.BBPS_FETCH_KEY
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.bbps.BBPSSelectOperatorViewModel
import it.services.instantpayment.viewModels.bbps.BbpsBillFetchViewModel
import it.services.instantpayment.viewModels.bbps.BbpsBillFetchViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService

class BbpsBillFetchFragment : DialogFragment() {

    private lateinit var operatorList: ArrayList<OperatorModel>
    private lateinit var binding: FragmentBbpsBillFetchBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var bbpsBillFetchViewModel: BbpsBillFetchViewModel
    private lateinit var progressDialog: AlertDialog

    private lateinit var operatorName: String
    private lateinit var operatorId: String
    private lateinit var service: String
    private lateinit var serviceId: String

    private val bbpsSelectOperatorViewModel:BBPSSelectOperatorViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBbpsBillFetchBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()
        progressDialog = CustomDialogs.getCustomProgressDialog(activity)
        operatorList = arguments?.getSerializable("operatorList") as ArrayList<OperatorModel>
        service = arguments?.getString("service", "").toString()
        serviceId = arguments?.getString("serviceId", "").toString()


        setUpViewModel()
        handleClicksAndEvents()
        setUpObservers()

        return binding.root
    }


    private fun setUpObservers() {
        bbpsBillFetchViewModel.billFetchData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(context, it.errMessage, Toast.LENGTH_LONG).show()
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    it.data?.let { data ->
                        val bundle = Bundle()
                        bundle.putString("data", data.toString())
                        bundle.putString("operatorId", operatorId)
                        bundle.putString("serviceId", serviceId)
                        //replaceFragment(BbpsBillDetailsFragment(), bundle)
                        findNavController().navigate(R.id.action_bbpsBillFetchFragment_to_bbpsBillDetailsFragment)
                    }
                }

            }
        }
    }

    private fun setUpViewModel() {
        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = BbpsBillFetchRepository(webService)

        bbpsBillFetchViewModel = ViewModelProvider(
            this,
            BbpsBillFetchViewModelFactory(repository)
        )[BbpsBillFetchViewModel::class.java]
    }

    private fun handleClicksAndEvents() {
        binding.apply {
            tvServiceName.text = service
            Glide.with(imgLogo).load(LoginActivity.logoImage).into(imgLogo)

            btnFetchBill.setOnClickListener {
                if (etNumber.text.toString().trim().equals("", true)) {
                    etNumber.requestFocus()
                    etNumber.error = "Required"
                    return@setOnClickListener
                } else {
                    val number = etNumber.text.toString().trim()
                    bbpsBillFetchViewModel.fetchBill(
                        MainActivity.LOGIN_SESSION,
                        BBPS_FETCH_KEY,
                        operatorId,
                        number
                    )
                }

            }

            tvOperator.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("operatorList", operatorList)

                //replaceFragment(BbpsOperatorListFragment(), bundle)
                findNavController().navigate(R.id.action_bbpsBillFetchFragment_to_bbpsOperatorListFragment,bundle)
                val fragment=BbpsOperatorListFragment()
                bbpsSelectOperatorViewModel.operatorData.observe(viewLifecycleOwner)
                {
                    operatorName = it.operatorName
                    operatorId = it.id
                    binding.tvOperator.text = operatorName
                }
            }
        }
    }


    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle

        if (fragment is BbpsOperatorListFragment) {
            fragment.operatorData.observe(viewLifecycleOwner)
            {
                operatorName = it.operatorName
                operatorId = it.id
                binding.tvOperator.text = operatorName
            }
        }

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_container, fragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }


}