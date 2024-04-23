package it.services.instantpayment.ui.recharge

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import it.services.instantpayment.R
import it.services.instantpayment.adapters.OperatorAdapter
import it.services.instantpayment.databinding.FragmentOperatorBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.BankModel
import it.services.instantpayment.models.OperatorModel

class OperatorFragment : Fragment() {

    private lateinit var binding: FragmentOperatorBinding
    private lateinit var operatorList: ArrayList<OperatorModel>
    private lateinit var operatorAdaptor:OperatorAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOperatorBinding.inflate(inflater, container, false)
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

    private fun setOperators() {
        operatorList = arguments?.getSerializable("operatorList") as ArrayList<OperatorModel>

        operatorAdaptor = OperatorAdapter(operatorList, object : AllClickInterface {
            override fun allClick(data: Any,type:String) {
                val operatorModel = data as OperatorModel
                val operatorName = operatorModel.operatorName
                val operatorId = operatorModel.id
                val serviceId = operatorModel.serviceId
                val operatorImage = operatorModel.operatorImage

                val bundle = Bundle()
                bundle.putString("operatorName", operatorName)
                bundle.putString("operatorId", operatorId)
                bundle.putString("serviceId", serviceId)
                    bundle.putString("operatorImage", operatorImage)

                replaceFragment(RechargeFragment(), bundle)

            }
        })
        binding.operatorRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.operatorRecycler.adapter = operatorAdaptor
    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.commit()
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

}