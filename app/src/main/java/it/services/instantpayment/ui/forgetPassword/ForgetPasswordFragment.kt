package it.services.instantpayment.ui.forgetPassword

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment() {

    private lateinit var binding:FragmentForgetPasswordBinding
    private lateinit var context:Context
    private lateinit var activity: Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentForgetPasswordBinding.inflate(inflater, container, false)


        return binding.root
    }
}