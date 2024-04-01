package it.services.instantpayment.ui.changePasswordPin

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentChangePasswordPinBinding


class ChangePasswordPinFragment : Fragment() {

    private lateinit var binding:FragmentChangePasswordPinBinding
    private lateinit var context:Context
    private lateinit var activity:Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentChangePasswordPinBinding.inflate(inflater, container, false)
        context=requireContext()
        activity=requireActivity()



        return binding.root
    }

}