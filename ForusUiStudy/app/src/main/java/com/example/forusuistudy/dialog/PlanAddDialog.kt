package com.example.forusuistudy.dialog

import android.app.Dialog
import android.content.Context
import android.database.DatabaseUtils
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.view.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.forusuistudy.R
import com.example.forusuistudy.databinding.DialogAddPlanBinding
import com.google.android.material.datepicker.MaterialDatePicker

/**
 *Created By Yelim ON 2021/10/13
 */
class PlanAddDialog: DialogFragment() {
    private lateinit var binding: DialogAddPlanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate<DialogAddPlanBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_add_plan,
            null,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etDialogPeriod.setOnClickListener {
        setDateRangePicker()
        }
    }

    private fun setDateRangePicker() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .build()

        dateRangePicker.show(activity?.supportFragmentManager!!, "tag")
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val diviceWidth = dialog?.window?.windowManager?.currentWindowMetrics?.bounds?.width()
        if (diviceWidth != null) {
            params?.width = (diviceWidth * 0.9).toInt()
        }
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }


}