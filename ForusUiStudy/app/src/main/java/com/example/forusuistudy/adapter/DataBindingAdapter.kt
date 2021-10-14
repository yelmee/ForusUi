package com.example.forusuistudy.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 *Created By Yelim ON 2021/10/14
 */

@SuppressLint("SetTextI18n")
@BindingAdapter("start","end")
fun bindProgressFromDate(view: TextView, start: String, end: String) {
    if (!start.isNullOrEmpty() && !end.isNullOrEmpty()) {
        view.text = "$start~$end"
    }
}