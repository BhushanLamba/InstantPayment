package it.services.instantpayment.ui.bbps

import android.app.Activity
import android.content.Context
import android.os.Bundle
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
import it.services.instantpayment.models.OperatorModel


class BbpsOperatorListFragment : Fragment() {

    private lateinit var binding: FragmentBbpsOperatorListBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
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


        return binding.root
    }

    private fun setOperators() {
        operatorList = arguments?.getSerializable("operatorList") as ArrayList<OperatorModel>

        val operatorAdaptor = OperatorAdapter(operatorList, object : AllClickInterface {
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