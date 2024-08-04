package it.services.instantpayment.ui.addUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentAdduserUserTypeBinding


class AdduserUserTypeFragment : Fragment() {

    private lateinit var binding:FragmentAdduserUserTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentAdduserUserTypeBinding.inflate(inflater, container, false)


        binding.apply {
            retailerLy.setOnClickListener {
                val bundle=Bundle()
                bundle.putString("newUserType","RT")
                replaceFragment(AddUserFragment(),bundle)
            }
        }
        return binding.root
    }

    private fun replaceFragment(fragment: Fragment, bundle:   Bundle) {
        fragment.arguments = bundle
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(it.services.instantpayment.R.id.frame_container, fragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }

}