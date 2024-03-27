package it.services.instantpayment.ui.reports

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentReceiptBinding
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.OutputStream
import java.util.Objects

class ReceiptFragment : Fragment() {

    private lateinit var binding:FragmentReceiptBinding
    private lateinit var context:Context
    private lateinit var activity:Activity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentReceiptBinding.inflate(inflater, container, false)

        context=requireContext()
        activity=requireActivity()

        getSetData()
        handleClicksAndEvents()

        return binding.root
    }

    private fun getSetData() {
        val dataStr=arguments?.getString("dataStr")

        try {
            val dataObject= dataStr?.let { JSONObject(it) }

            val keyList=ArrayList<String>()
            val valueList=ArrayList<String>()

            val keysIterator=dataObject?.keys()
            var value=""

            while (keysIterator?.hasNext() == true)
            {
                val currentKey=keysIterator.next()
                val currentValue=dataObject.getString(currentKey)

                if (!(currentValue.equals("") || currentValue.equals("null",true)))
                {
                    keyList.add(currentKey)
                    valueList.add(currentValue)
                }
            }

            for(position in 0 until keyList.size)
            {
                val rootLayoutParams=LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                rootLayoutParams.setMargins(0,15,0,15)

                val textViewParams=LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1f)
                textViewParams.setMargins(20,0,20,0)

                val  innerLayout=LinearLayout(context)
                innerLayout.orientation = LinearLayout.HORIZONTAL
                innerLayout.layoutParams = rootLayoutParams

                value = keyList[position]

                val tvKey = TextView(requireContext())
                tvKey.layoutParams = textViewParams
                tvKey.gravity = Gravity.START
                tvKey.textSize = 14f
                tvKey.typeface = Typeface.DEFAULT_BOLD
                tvKey.setTextColor(resources.getColor(R.color.black))
                tvKey.text = value

                value = valueList[position]

                val tvValue = TextView(requireContext())
                tvValue.layoutParams = textViewParams
                tvValue.gravity = Gravity.END
                tvValue.textSize = 14f
                tvValue.typeface = Typeface.DEFAULT_BOLD
                tvValue.setTextColor(resources.getColor(R.color.black))
                tvValue.text = value

                innerLayout.addView(tvKey)
                innerLayout.addView(tvValue)

                val horizontalLine = View(requireContext())
                horizontalLine.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2)
                horizontalLine.setPadding(0, 10, 0, 10)
                horizontalLine.setBackgroundColor(Color.rgb(0, 0, 0))

                binding.dynamicLayout.addView(horizontalLine)
                binding.dynamicLayout.addView(innerLayout)


            }

        }
        catch (e:Exception)
        {

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

    private fun bitmapToFile(imageBitmap: Bitmap): Uri?
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



        Toast.makeText(context,"File Saved", Toast.LENGTH_LONG).show()

        return uri
    }


    private fun takeScreenshot(view: ScrollView, height:Int, width:Int): Bitmap
    {
        val bitmap= Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)
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