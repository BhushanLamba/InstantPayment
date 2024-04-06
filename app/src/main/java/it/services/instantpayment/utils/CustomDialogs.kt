package it.services.instantpayment.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import it.services.instantpayment.databinding.CustomProgressDialogBinding
import it.services.instantpayment.databinding.DmtConfirmationDialogBinding
import it.services.instantpayment.databinding.MessageDialogBinding
import it.services.instantpayment.databinding.MpinDialogBinding
import it.services.instantpayment.ui.login.LoginActivity

object CustomDialogs {
    fun getCustomProgressDialog(activity: Activity): AlertDialog {

        val binding = CustomProgressDialogBinding.inflate(LayoutInflater.from(activity))
        Glide.with(binding.imgLogo).load(LoginActivity.logoImage).into(binding.imgLogo)

        val pDialogBuilder = AlertDialog.Builder(activity)

        pDialogBuilder.setView(binding.root)

        val pDialog = pDialogBuilder.create()

        pDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        pDialog.setCancelable(false)
        return pDialog

    }

    fun getMessageDialog(activity: Activity,message:String,showCancelButton:Boolean): AlertDialog {

        val binding = MessageDialogBinding.inflate(LayoutInflater.from(activity))

        val messageDialogBuilder = AlertDialog.Builder(activity)

        messageDialogBuilder.setView(binding.root)

        val messageDialog = messageDialogBuilder.create()

        messageDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        messageDialog.setCancelable(false)


        binding.apply {
            tvMessage.text=message
            if (!showCancelButton)
            {
                btnCancel.visibility=View.GONE
            }

            btnOk.setOnClickListener { messageDialog.dismiss() }
            btnCancel.setOnClickListener { messageDialog.dismiss() }
        }
        messageDialog.show()
        return messageDialog

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