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
import it.services.instantpayment.databinding.FragmentAePSDeviceBinding
import it.services.instantpayment.utils.SharedPref


class AePSDeviceFragment : Fragment() {

    private lateinit var binding: FragmentAePSDeviceBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    private var device = ""
    private var devicePackage = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAePSDeviceBinding.inflate(inflater, container, false)

        context = requireContext()
        activity = requireActivity()

        clickEvents()
        return binding.root
    }

    private fun clickEvents() {

        binding.apply {


            rgDevices.setOnCheckedChangeListener { _, _ ->

                if (rbMantra.isChecked) {
                    device = "Mantra"
                    devicePackage = "com.mantra.rdservice"
                } else if (rbMantraL1.isChecked) {
                    device = "Mantra l1"
                    devicePackage = "com.mantra.mfs110.rdservice"
                } else if (rbMorpho.isChecked) {
                    device = "Morpho"
                    devicePackage = "com.scl.rdservice"
                } else if (rbMorpho.isChecked) {
                    device = "Morpho L1"
                    devicePackage = "com.idemia.l1rdservice"
                } else if (rbStartek.isChecked) {
                    device = "Startek"
                    devicePackage = "com.acpl.registersdk"
                } else if (rbSecugen.isChecked) {
                    device = "Secugen"
                    devicePackage = "com.secugen.rdservice"
                }

            }



            tvProceed.setOnClickListener {
                if (isPackageExisted(devicePackage)) {
                    SharedPref.setString(context, SharedPref.DEVICE_NAME, device)
                    SharedPref.setString(context, SharedPref.DEVICE_PACKAGE, devicePackage)

                    getActivity()?.supportFragmentManager?.popBackStackImmediate()
                } else {
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