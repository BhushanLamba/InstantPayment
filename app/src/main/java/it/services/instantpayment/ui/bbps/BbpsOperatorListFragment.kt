package it.services.instantpayment.ui.bbps

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
import it.services.instantpayment.adapters.OperatorAdapter
import it.services.instantpayment.databinding.FragmentBbpsOperatorListBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.BankModel
import it.services.instantpayment.models.OperatorModel


class BbpsOperatorListFragment : Fragment() {

    private lateinit var binding: FragmentBbpsOperatorListBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var operatorAdaptor:OperatorAdapter
    private lateinit var operatorList: ArrayList<OperatorModel>

    private val operatorLiveData = MutableLiveData<OperatorModel>()
    val operatorData: LiveData<OperatorModel>
        get() = operatorLiveData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBbpsOperatorListBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()

        setOperators()

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
        val filteredList = ArrayList<OperatorModel>()

        for (position in 0 until operatorList.size) {

            if (operatorList[position].operatorName.lowercase().contains(searchText.lowercase())
            ) {
                filteredList.add(operatorList[position])
            }
        }

        if (filteredList.isNotEmpty()) {
            operatorAdaptor.filterData(filteredList)
        }


    }

    private fun setOperators() {
        operatorList = arguments?.getSerializable("operatorList") as ArrayList<OperatorModel>

        operatorAdaptor = OperatorAdapter(operatorList, object : AllClickInterface {
            override fun allClick(data: Any,type:String) {
                val operatorModel = data as OperatorModel

                operatorLiveData.postValue(operatorModel)
                fragmentManager?.popBackStackImmediate()

            }
        })
        binding.operatorRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.operatorRecycler.adapter = operatorAdaptor
    }

}