package it.services.instantpayment.ui.dmt

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import it.services.instantpayment.R
import it.services.instantpayment.databinding.BankItemBinding
import it.services.instantpayment.databinding.FragmentDmtReceiptBinding
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.OutputStream
import java.util.Objects

class DmtReceiptFragment : Fragment() {

    private lateinit var binding: FragmentDmtReceiptBinding
    private lateinit var context:Context
    private lateinit var activity:Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentDmtReceiptBinding.inflate(inflater, container, false)
        context=requireContext()
        activity=requireActivity()
        getSetData()
        handleClicksAndEvents()


        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun getSetData() {
        val responseStr = arguments?.getString("data")
        val responseObject = JSONObject(responseStr.toString())

        try {

            val message = responseObject.getString("Message")
            val dataArray = responseObject.getJSONArray("Data")
            val dataObject = dataArray.getJSONObject(0)
            val accountNo = dataObject.getString("AccountNo")
            val beneName = dataObject.getString("BeneName")
            val amount = dataObject.getString("Amount")
            val charge = dataObject.getString("Charge")
            val status = dataObject.getString("Status")
            val walletBalance = dataObject.getString("CurrentBalance")
            val orderId = dataObject.getString("TxnID")
            val dateTime = dataObject.getString("TxnDate")

            binding.apply {
                tvMessage.text = message
                tvAmount.text = "₹ $amount"
                tvCharge.text = "₹ $charge"
                tvAccountNo.text = accountNo
                tvBene.text = beneName
                tvOrderId.text = orderId
                tvDateTime.text = dateTime
                tvStatus.text = status
                tvWallet.text = walletBalance
            }


        }
        catch (ignore: Exception) {
            Toast.makeText(context, "There are some issues. We are fixing it.", Toast.LENGTH_LONG)
                .show()
            Log.d("exception", "getSetData: " + ignore.localizedMessage)
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun handleClicksAndEvents() {
        binding.apply {
            tvDownload.setOnClickListener {
                val bitmap: Bitmap = takeScreenshot(binding.receiptLy,binding.receiptLy.getChildAt(0).height,binding.receiptLy.getChildAt(0).width)
                bitmapToFile(bitmap)
            }

            shareLy.setOnClickListener {
                val bitmap: Bitmap = takeScreenshot(binding.receiptLy,binding.receiptLy.getChildAt(0).height,binding.receiptLy.getChildAt(0).width)
                val uri=bitmapToFile(bitmap)
                shareReceipt(uri,false)
            }
            whatsAppLy.setOnClickListener {
                val bitmap: Bitmap = takeScreenshot(binding.receiptLy,binding.receiptLy.getChildAt(0).height,binding.receiptLy.getChildAt(0).width)
                val uri=bitmapToFile(bitmap)
                shareReceipt(uri,true)
            }
        }
    }

    private fun shareReceipt(uri: Uri?, isWhatsApp:Boolean) {
        val intent= Intent(Intent.ACTION_SEND)
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
        val contentValues= ContentValues()
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


    private fun takeScreenshot(view: ScrollView, height:Int, width:Int):Bitmap
    {
        val bitmap= Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
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
}