package it.services.instantpayment.ui.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import it.services.instantpayment.R
import it.services.instantpayment.adapters.OperatorAdapter
import it.services.instantpayment.databinding.FragmentOperatorBinding
import it.services.instantpayment.interfaces.AllClickInterface
import it.services.instantpayment.models.OperatorModel

class OperatorFragment : Fragment() {

    private lateinit var binding: FragmentOperatorBinding
    private lateinit var operatorList: ArrayList<OperatorModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOperatorBinding.inflate(inflater, container, false)
        setOperators()
        return binding.root
    }

    private fun setOperators() {
        operatorList = arguments?.getSerializable("operatorList") as ArrayList<OperatorModel>

        val operatorAdaptor = OperatorAdapter(operatorList, object : AllClickInterface {
            override fun allClick(data: Any,type:String) {
                val operatorModel = data as OperatorModel
                val operatorName = operatorModel.operatorName
                val operatorId = operatorModel.id
                val serviceId = operatorModel.serviceId

                val bundle = Bundle()
                bundle.putString("operatorName", operatorName)
                bundle.putString("operatorId", operatorId)
                bundle.putString("serviceId", serviceId)

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
}