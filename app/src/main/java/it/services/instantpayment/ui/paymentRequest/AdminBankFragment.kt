package it.services.instantpayment.ui.paymentRequest

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import it.services.instantpayment.adapters.AdminBankAdapter
import it.services.instantpayment.databinding.FragmentAdminBankBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.AdminBanksModel


class AdminBankFragment : Fragment() {

    private lateinit var binding: FragmentAdminBankBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    private lateinit var bankList: ArrayList<AdminBanksModel>
    private lateinit var bankAdapter: AdminBankAdapter

    private val bankLiveData = MutableLiveData<AdminBanksModel>()
    val bankData: LiveData<AdminBanksModel>
        get() = bankLiveData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBankBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()

        setBank()
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(inputChar: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchData(inputChar.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        return binding.root
    }

    private fun searchData(searchText: String) {
        val filteredList = ArrayList<AdminBanksModel>()

        for (position in 0 until bankList.size) {

            if (bankList[position].bankName.lowercase().contains(searchText.lowercase())
            ) {
                filteredList.add(bankList[position])
            }
        }

        if (filteredList.isNotEmpty()) {
            bankAdapter.filterData(filteredList)
        }
    }

    private fun setBank() {
        bankList = arguments?.getSerializable("bankList") as ArrayList<AdminBanksModel>

        bankAdapter = AdminBankAdapter(bankList, object : AllClickInterface {
            override fun allClick(data: Any, type: String) {
                val bankModel = data as AdminBanksModel

                bankLiveData.postValue(bankModel)
                fragmentManager?.popBackStackImmediate()

            }
        })
        binding.recyler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyler.adapter = bankAdapter
    }
}