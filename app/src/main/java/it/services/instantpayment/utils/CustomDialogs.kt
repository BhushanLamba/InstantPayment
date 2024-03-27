package it.services.instantpayment.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Toast
import it.services.instantpayment.databinding.CustomProgressDialogBinding
import it.services.instantpayment.databinding.DmtConfirmationDialogBinding
import it.services.instantpayment.databinding.MpinDialogBinding

object CustomDialogs {
    fun getCustomProgressDialog(activity: Activity): AlertDialog {

        val binding = CustomProgressDialogBinding.inflate(LayoutInflater.from(activity))

        val pDialogBuilder = AlertDialog.Builder(activity)

        pDialogBuilder.setView(binding.root)

        val pDialog = pDialogBuilder.create()

        pDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        pDialog.setCancelable(false)
        return pDialog

    }

    fun getMpinDialog(activity: Activity) :AlertDialog
    {
        val binding = MpinDialogBinding.inflate(LayoutInflater.from(activity))

        val mpinDialogBuilder=AlertDialog.Builder(activity)

        mpinDialogBuilder.setView(binding.root)

        val mpinDialog=mpinDialogBuilder.create()

        mpinDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mpinDialog.setCancelable(false)
        mpinDialog.show()


        return mpinDialog

    }

    fun getDmtMpinDialog(activity: Activity) :AlertDialog
    {
        val binding = DmtConfirmationDialogBinding.inflate(LayoutInflater.from(activity))

        val mpinDialogBuilder=AlertDialog.Builder(activity)

        mpinDialogBuilder.setView(binding.root)

        val mpinDialog=mpinDialogBuilder.create()

        mpinDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mpinDialog.setCancelable(false)
        mpinDialog.show()


        return mpinDialog

    }
}