package it.services.instantpayment.ui.dmt

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.chaos.view.PinView
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.adapters.BeneListAdapter
import it.services.instantpayment.databinding.FragmentBeneListBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.BankModel
import it.services.instantpayment.models.BeneListModel
import it.services.instantpayment.repository.BeneListRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.ui.login.LoginActivity
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.dmt.beneList.BeneListViewModel
import it.services.instantpayment.viewModels.dmt.beneList.BeneListViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class BeneListFragment : Fragment() {

    private lateinit var binding: FragmentBeneListBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var senderNumber: String
    private lateinit var beneListViewModel: BeneListViewModel
    private lateinit var progressDialog: AlertDialog
    private lateinit var beneList:ArrayList<BeneListModel>
    private lateinit var beneAdapter:BeneListAdapter

    companion object
    {
        var SENDER_ID=""
        var SENDER_NAME=""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBeneListBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()
        progressDialog = CustomDialogs.getCustomProgressDialog(activity)

        setUpViews()
        setUpViewModel()
        setUpObservers()
        handleClickAndEvents()

        return binding.root
    }

    private fun handleClickAndEvents() {
        binding.apply {
            Glide.with(imgLogo).load(LoginActivity.logoImage).into(imgLogo)

            btnAddBene.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("senderMobile", senderNumber)
                val addBeneFragment = AddBeneFragment()

                replaceFragment(addBeneFragment, bundle)
                addBeneFragment.addBeneData.observe(viewLifecycleOwner)
                {
                    beneListViewModel.getBeneList(
                        MainActivity.LOGIN_SESSION,
                        ApiKeys.BENE_LIST_KEY,
                        senderNumber
                    )
                }
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
                        if (::beneList.isInitialized)
                        {
                            beneAdapter.filterData(beneList)
                            binding.beneRecycler.visibility=View.VISIBLE
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
    }

    private fun searchData(searchText: String) {
        val filteredList = ArrayList<BeneListModel>()

        if (::beneList.isInitialized)
        {
            for (position in 0 until beneList.size) {

                if (beneList[position].accountNumber.lowercase().contains(searchText.lowercase()) ||
                    beneList[position].beneName.lowercase().contains(searchText.lowercase())
                ) {
                    filteredList.add(beneList[position])
                }
            }

            if (filteredList.isNotEmpty()) {
                beneAdapter.filterData(filteredList)
            }
            else
            {
                binding.beneRecycler.visibility=View.GONE
            }
        }


    }


    private fun setUpObservers() {
        beneListViewModel.beneListData.observe(viewLifecycleOwner)
        { response ->
            when (response) {



                is Response.Success -> {
                    progressDialog.dismiss()
                    beneList= response.data!!
                    beneAdapter = BeneListAdapter(response.data, object : AllClickInterface {
                        @SuppressLint("SetTextI18n")
                        override fun allClick(data: Any, amount: String) {
                            val beneModel = data as BeneListModel
                            val beneName = beneModel.beneName
                            val accountNumber = beneModel.accountNumber
                            val ifsc = beneModel.ifsc
                            val bankName = beneModel.bankName
                            val beneId = beneModel.beneId

                            val mpinDialog = CustomDialogs.getDmtMpinDialog(activity)

                            val btnCancel: AppCompatButton =
                                mpinDialog.findViewById(R.id.btn_cancel)
                            val btnSubmit: AppCompatButton =
                                mpinDialog.findViewById(R.id.btn_submit)
                            val mpinView: PinView = mpinDialog.findViewById(R.id.otp_pin_view)
                            val tvBene: TextView = mpinDialog.findViewById(R.id.tv_bene)
                            val tvBank: TextView = mpinDialog.findViewById(R.id.tv_bank)
                            val tvAccountNo: TextView =
                                mpinDialog.findViewById(R.id.tv_account_no)
                            val tvAmount: TextView = mpinDialog.findViewById(R.id.tv_amount)
                            val rbImps: RadioButton = mpinDialog.findViewById(R.id.rb_imps)
                            val rgTransactionType: RadioGroup = mpinDialog.findViewById(R.id.rg_transaction_type)

                            if (SenderMobileVerificationFragment.sType.equals("UPI",true)) {
                                rgTransactionType.visibility=View.GONE
                            }

                            tvBene.text = beneName
                            tvBank.text = bankName
                            tvAccountNo.text = accountNumber
                            tvAmount.text = "₹ $amount"

                            btnCancel.setOnClickListener { mpinDialog.dismiss() }

                            btnSubmit.setOnClickListener {
                                val mpin = mpinView.text.toString().trim()

                                if (mpin.length == 4) {
                                    mpinDialog.dismiss()

                                    var transactionMode =""
                                    transactionMode = if (rbImps.isChecked) {
                                        "IMPS"
                                    } else {
                                        "NEFT"
                                    }

                                    beneListViewModel.payBene(
                                        MainActivity.LOGIN_SESSION,
                                        ApiKeys.PAY_BENE_KEY,
                                        senderNumber,
                                        beneName,
                                        accountNumber,
                                        ifsc,
                                        bankName,
                                        beneId,
                                        amount, mpin,transactionMode
                                    )

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please enter correct mpin.",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }


                        }
                    })
                    binding.apply {
                        Log.d("bene", "setUpObservers: $response")
                        beneRecycler.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.beneRecycler.adapter = beneAdapter
                    }
                }

                is Response.Loading->
                {
                    progressDialog.show()
                }
                is Response.Error->
                {
                    progressDialog.dismiss()
                }
            }
        }

        beneListViewModel.payBeneData.observe(viewLifecycleOwner)
        { response ->

            when (response) {
                is Response.Error -> {
                    progressDialog.dismiss()
                    val alertDialog = AlertDialog.Builder(context)
                    alertDialog.setMessage(response.errMessage)
                    alertDialog.setPositiveButton("Ok", null)
                    alertDialog.show()
                }

                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    val dataObject = response.data
                    val bundle = Bundle()
                    bundle.putString("data", dataObject.toString())
                    replaceFragment(DmtReceiptFragment(), bundle)
                }
            }

        }
    }


    private fun setUpViewModel() {

        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = BeneListRepository(webService)
        beneListViewModel = ViewModelProvider(
            this,
            BeneListViewModelFactory(repository)
        )[BeneListViewModel::class.java]

        beneListViewModel.getBeneList(
            MainActivity.LOGIN_SESSION,
            ApiKeys.BENE_LIST_KEY,
            senderNumber
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setUpViews() {
        val responseStr = arguments?.getString("response").toString()
        senderNumber = arguments?.getString("number", "").toString()
        try {
            val responseData = JSONObject(responseStr)
            val dataArray = responseData.getJSONArray("Data")

            val dataObject = dataArray.getJSONObject(0)
            var name =""
            var limit =""
            if (SenderMobileVerificationFragment.sType.equals("DMT3",true) ||
                SenderMobileVerificationFragment.sType.equals("UPI",true))
            {
                name= dataObject.getString("SenderName")
                limit= dataObject.getString("Limit")
                SENDER_ID=dataObject.getString("Id")
                SENDER_NAME=name

            }
            else
            {
                name= dataObject.getString("first_name")
                limit= dataObject.getString("available_limit")
            }

            binding.apply {
                tvRemitterNameMobile.text = "$name | $senderNumber"
                tvRemitterLimit.text = limit
            }

        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
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
}