package it.services.instantpayment.ui.addUser

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentViewUserBinding

class ViewUserFragment : Fragment() {
    private lateinit var binding:FragmentViewUserBinding
    private lateinit var context:Context
    private lateinit var activity: Activity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentViewUserBinding.inflate(inflater, container, false)
        context=requireContext()
        activity=requireActivity()



        return binding.root
    }
}