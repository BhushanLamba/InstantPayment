package it.services.instantpayment.ui.recharge

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentRechargeTransactionSlipBinding
import it.services.instantpayment.ui.login.LoginActivity
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.OutputStream
import java.util.Objects


class RechargeTransactionSlipFragment : Fragment() {

    private lateinit var binding: FragmentRechargeTransactionSlipBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRechargeTransactionSlipBinding.inflate(inflater, container, false)

        context = requireContext()
        activity = requireActivity()

        getSetData()
        handleClicksAndEvents()

        return binding.root
    }

    private fun handleClicksAndEvents() {

        binding.apply {
            tvDownload.setOnClickListener {
                val bitmap:Bitmap= takeScreenshot(binding.receiptLy,binding.receiptLy.getChildAt(0).height,binding.receiptLy.getChildAt(0).width)
                bitmapToFile(bitmap)
            }

            shareLy.setOnClickListener {
                val bitmap:Bitmap= takeScreenshot(binding.receiptLy,binding.receiptLy.getChildAt(0).height,binding.receiptLy.getChildAt(0).width)
                val uri=bitmapToFile(bitmap)
                shareReceipt(uri,false)
            }
            whatsAppLy.setOnClickListener {
                val bitmap:Bitmap= takeScreenshot(binding.receiptLy,binding.receiptLy.getChildAt(0).height,binding.receiptLy.getChildAt(0).width)
                val uri=bitmapToFile(bitmap)
                shareReceipt(uri,true)
            }
        }
    }

    private fun shareReceipt(uri: Uri?,isWhatsApp:Boolean) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="image/*"
        intent.putExtra(Intent.EXTRA_STREAM,uri)
        if (isWhatsApp)
        {
            intent.`package`="com.whatsapp"
        }
        startActivity(intent)
    }

    private fun bitmapToFile(imageBitmap:Bitmap): Uri?
    {
        val fos: OutputStream
        val contentResolver=activity.contentResolver
        val contentValues=ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image" + System.currentTimeMillis() + ".jpg")
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "InstantPayment")
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        try {
            fos = contentResolver.openOutputStream(Objects.requireNonNull<Uri>(uri))!!
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            Objects.requireNonNull(fos)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }



        Toast.makeText(context,"File Saved",Toast.LENGTH_LONG).show()

        return uri
    }


    private fun takeScreenshot(view: ScrollView,height:Int,width:Int):Bitmap
    {
        val bitmap= Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        val canvas=Canvas(bitmap)
        val bgDrawable=view.background
        if (bgDrawable!=null)
        {
            bgDrawable.draw(canvas)
        }
        else
        {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)

        return bitmap
    }

    @SuppressLint("SetTextI18n")
    private fun getSetData() {
        val responseStr = arguments?.getString("data")
        val responseObject = JSONObject(responseStr.toString())

        try {

            val message = responseObject.getString("Message")
            val dataArray = responseObject.getJSONArray("Data")
            val dataObject = dataArray.getJSONObject(0)
            val operator = dataObject.getString("Operator")
            val txnId = dataObject.getString("TxnID")
            val number = dataObject.getString("MobileNo")
            val date = dataObject.getString("TxnDate")
            val status = dataObject.getString("Status")
            val walletBalance = dataObject.getString("CurrentBalance")
            val amount = dataObject.getString("Amount")

            binding.apply {
                Glide.with(imgLogo).load(LoginActivity.logoImage).into(imgLogo)

                tvMessage.text = message
                tvOperator.text = operator
                tvOrderId.text = txnId
                tvNumber.text = number
                tvDateTime.text = date
                tvStatus.text = status
                tvWallet.text = walletBalance
                tvAmount.text = "₹ $amount"
            }


        }
        catch (ignore: Exception) {
            Toast.makeText(context, "There are some issues. We are fixing it.", Toast.LENGTH_LONG)
                .show()
            Log.d("exception", "getSetData: " + ignore.localizedMessage)
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }
}