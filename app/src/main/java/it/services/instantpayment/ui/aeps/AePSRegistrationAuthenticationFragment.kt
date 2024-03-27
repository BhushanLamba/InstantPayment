package it.services.instantpayment.ui.aeps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import it.services.instantpayment.databinding.FragmentAePSRegistrationAuthenticationBinding
import it.services.instantpayment.device.PidData
import it.services.instantpayment.utils.SharedPref
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister


class AePSRegistrationAuthenticationFragment : Fragment() {

    private lateinit var binding: FragmentAePSRegistrationAuthenticationBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    private var device=""
    private var devicePackage=""

    private lateinit var serializer: Serializer
    private lateinit var pidData: PidData

    private var isValidAadhaar=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAePSRegistrationAuthenticationBinding.inflate(inflater, container, false)
        context=requireContext()
        activity=requireActivity()
        device=SharedPref.getString(context,SharedPref.DEVICE_NAME).toString()
        devicePackage=SharedPref.getString(context,SharedPref.DEVICE_PACKAGE).toString()
        serializer=Persister()

        clickEvents();


        return binding.root
    }

    private fun clickEvents() {
        binding.apply {
            tvProceed.setOnClickListener {
                if (isPackageExisted(devicePackage))
                {

                }
                else
                {
                    AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Please install $device RD Service")
                        .setPositiveButton(
                            "Install"
                        ) { _: DialogInterface?, i: Int ->
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=$devicePackage")
                                    )
                                )
                            } catch (anfe: ActivityNotFoundException) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=$devicePackage")
                                    )
                                )
                            }
                        }
                        .setNegativeButton("Cancel", null)
                        .setCancelable(false)
                        .show()
                }
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun isPackageExisted(targetPackage: String): Boolean {
        val packages: List<ApplicationInfo>
        val pm: PackageManager = requireActivity().packageManager
        packages = pm.getInstalledApplications(0)
        for (packageInfo in packages) {
            if (packageInfo.packageName == targetPackage) return true
        }
        return false
    }
}