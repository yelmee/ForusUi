package com.example.forusuistudy.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.database.DatabaseUtils
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.forusuistudy.R
import com.example.forusuistudy.data.Plan
import com.example.forusuistudy.databinding.DialogAddPlanBinding
import com.example.forusuistudy.ui.CalendarFragment
import com.example.forusuistudy.utils.CalendarUtils.Companion.changeLongToString
import com.example.forusuistudy.utils.CalendarUtils.Companion.divideStartToEnd
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener

/**
 *Created By Yelim ON 2021/10/13
 */
class PlanAddDialog: DialogFragment() {
    private lateinit var binding: DialogAddPlanBinding
    private var dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
        .setTitleText("Select dates")
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //isCancelable = false
    }

    companion object{
        var onDismissListener: ((Plan) -> Unit)? = null
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etDialogPeriod.setOnClickListener {
        setDateRangePicker()
        }

        binding.ivDialogConfirm.setOnClickListener {
        addPlan()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return super.onCreateDialog(savedInstanceState)
        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.setCanceledOnTouchOutside(false)

        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        return dialog
    }

    private fun setDateRangePicker() {

        dateRangePicker.show(activity?.supportFragmentManager!!, "tag")

        dateRangePicker.addOnPositiveButtonClickListener{ selection ->
            val startDate = selection.first
            val endDate = selection.second
            
            Log.d("jylLog", "startDate: +$startDate, $endDate")

            binding.etDialogPeriod.text = changeLongToString(startDate)+"~"+ changeLongToString(endDate)
        }
    }


    private fun addPlan() {
        val title = binding.etDialogTitle.text.toString()
        val period = binding.etDialogPeriod.text.toString()
        val url = binding.etDialogUrl.text.toString()
//        val tag= binding.etDialogTag.text
        val plan = Plan(1, title, divideStartToEnd(period).first, divideStartToEnd(period).second, url, 123, 50)
//        addCalendarPlan(plan)

        onDismissListener?.invoke(plan)
        dialog?.dismiss()
    }
}