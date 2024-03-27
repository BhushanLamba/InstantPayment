package it.services.instantpayment.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.services.instantpayment.databinding.FragmentBottomDateBinding
import it.services.instantpayment.interfaces.AllClickInterface
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class BottomDateFragment(
    private val context: Context,
    private val activity: Activity,
    private var fromDate: String,
    private var toDate: String,
    private val allClickInterface: AllClickInterface
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomDateBinding

    private lateinit var apiDateFormat:SimpleDateFormat
    private lateinit var viewDateFormat:SimpleDateFormat


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomDateBinding.inflate(inflater, container, false)

        apiDateFormat=SimpleDateFormat("yyyy-MM-dd", Locale.US)
        viewDateFormat=SimpleDateFormat("dd-MM-yyyy", Locale.US)

        binding.apply {
            tvFromDate.text=fromDate
            tvToDate.text=toDate
        }

        handleClicks()

        return binding.root
    }

    private fun handleClicks() {

        binding.apply {
            imgClose.setOnClickListener { dismiss() }

            fromDateLy.setOnClickListener {
                val calender=Calendar.getInstance()
                val currentYear=calender.get(Calendar.YEAR)
                val currentMonth=calender.get(Calendar.MONTH)
                val today=calender.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog=DatePickerDialog(context, { _, year, month, dayOfMonth ->

                    val selectedDate=Calendar.getInstance()
                    selectedDate.set(year,month,dayOfMonth)
                    tvFromDate.text=viewDateFormat.format(selectedDate.time)
                    fromDate=apiDateFormat.format(selectedDate.time)


                },currentYear,currentMonth,today)

                datePickerDialog.show()
            }

            toDateLy.setOnClickListener {
                val calender=Calendar.getInstance()
                val currentYear=calender.get(Calendar.YEAR)
                val currentMonth=calender.get(Calendar.MONTH)
                val today=calender.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog=DatePickerDialog(context, { _, year, month, dayOfMonth ->

                    val selectedDate=Calendar.getInstance()
                    selectedDate.set(year,month,dayOfMonth)
                    tvToDate.text=viewDateFormat.format(selectedDate.time)
                    toDate=apiDateFormat.format(selectedDate.time)


                },currentYear,currentMonth,today)

                datePickerDialog.show()
            }

            btnSearch.setOnClickListener {
                allClickInterface.allClick(fromDate,toDate)
                dismiss() }
        }

    }


}