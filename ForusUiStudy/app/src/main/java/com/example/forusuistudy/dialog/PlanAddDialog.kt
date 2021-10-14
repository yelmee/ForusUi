package com.example.forusuistudy.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import com.example.forusuistudy.R

/**
 *Created By Yelim ON 2021/10/13
 */
class PlanAddDialog(context: Context) {
    private var context: Context = context

    fun callDialog() {
        val dig = Dialog(context)

        dig.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val params: WindowManager.LayoutParams? = dig.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dig.setContentView(R.layout.dialog_add_plan)
        dig.window?.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dig.show()
    }

}