package it.services.instantpayment.ui.addUser

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaos.view.PinView
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.adapters.ViewUserAdapter
import it.services.instantpayment.databinding.FragmentViewUserBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.repository.Response
import it.services.instantpayment.repository.ViewUserRepository
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.viewUser.ViewUserViewModel
import it.services.instantpayment.viewModels.viewUser.ViewUserViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class ViewUserFragment : Fragment() {
    private lateinit var binding: FragmentViewUserBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    private lateinit var progressDialog:AlertDialog

    private lateinit var viewUserViewModel: ViewUserViewModel

    private lateinit var transferAmount:String
    private lateinit var transactionType:String
    private lateinit var transferUserId:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewUserBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()

        progressDialog=CustomDialogs.getCustomProgressDialog(activity)

        setUpViewModel()
        setUpObserve()

        return binding.root
    }

    private fun setUpObserve() {
        viewUserViewModel.viewUserData.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is Response.Error -> {
                    progressDialog.dismiss()
                }
                is Response.Loading -> {
                    progressDialog.show()
                }
                is Response.Success ->{
                    progressDialog.dismiss()
                    binding.recycler.layoutManager=LinearLayoutManager(context,RecyclerView.VERTICAL,false)
                    binding.recycler.adapter=ViewUserAdapter(it.data!!,object :AllClickInterface{
                        override fun allClick(data: Any, extra: String) {
                            val dataObject=data as JSONObject
                            transferAmount=dataObject.getString("amount")
                            transactionType=dataObject.getString("transactionType")
                            transferUserId=dataObject.getString("transferUserId")

                            shopMpinDialog()

                        }

                    })
                }
            }

        }

        viewUserViewModel.creditDebitData.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is Response.Error -> {
                    progressDialog.dismiss()
                    CustomDialogs.getMessageDialog(activity,it.errMessage.toString(),false)
                }
                is Response.Loading ->{
                    progressDialog.show()
                }
                is Response.Success -> {
                    progressDialog.dismiss()
                    CustomDialogs.getMessageDialog(activity,it.data.toString(),false)


                }
            }
        }
    }

    private fun shopMpinDialog() {
        val mpinDialog = CustomDialogs.getMpinDialog(activity)

        val btnCancel: AppCompatButton = mpinDialog.findViewById(R.id.btn_cancel)
        val btnSubmit: AppCompatButton = mpinDialog.findViewById(R.id.btn_submit)
        val mpinView: PinView = mpinDialog.findViewById(R.id.otp_pin_view)
        val tvInfo: TextView = mpinDialog.findViewById(R.id.tv_info)

        tvInfo.visibility=View.GONE


        btnCancel.setOnClickListener { mpinDialog.dismiss() }

        btnSubmit.setOnClickListener {
            val mpin = mpinView.text.toString().trim()

            if (mpin.length == 4) {
                mpinDialog.dismiss()
                viewUserViewModel.creditDebitUser(MainActivity.LOGIN_SESSION,"DebitCredit001",transactionType,
                    transferUserId,transferAmount,mpin)

            } else {
                Toast.makeText(context, "Please enter correct mpin.", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setUpViewModel() {
        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = ViewUserRepository(webService)

        viewUserViewModel = ViewModelProvider(
            this,
            ViewUserViewModelFactory(repository)
        )[ViewUserViewModel::class.java]

        viewUserViewModel.viewUser(MainActivity.LOGIN_SESSION,"ViewUser001",MainActivity.USER_TYPE)
    }
}