package it.services.instantpayment.ui.dmt

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
import it.services.instantpayment.adapters.BankAdapter
import it.services.instantpayment.databinding.FragmentBankBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.BankModel
import java.util.Locale

class BankFragment : Fragment() {

    private lateinit var binding: FragmentBankBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var bankAdapter: BankAdapter

    private lateinit var bankList: ArrayList<BankModel>

    private val bankLiveData = MutableLiveData<BankModel>()
    val bankData: LiveData<BankModel>
        get() = bankLiveData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBankBinding.inflate(inflater, container, false)
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
        val filteredList = ArrayList<BankModel>()

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
        bankList = arguments?.getSerializable("bankList") as ArrayList<BankModel>

        bankAdapter = BankAdapter(bankList, object : AllClickInterface {
            override fun allClick(data: Any, type: String) {
                val bankModel = data as BankModel

                bankLiveData.postValue(bankModel)
                fragmentManager?.popBackStackImmediate()

            }
        })
        binding.recyler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyler.adapter = bankAdapter
    }


}