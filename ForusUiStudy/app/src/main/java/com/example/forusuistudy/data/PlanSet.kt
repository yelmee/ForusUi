package com.example.forusuistudy.data

import org.joda.time.DateTime
import java.util.*

/**
 *Created By Yelim ON 2021/10/08
 */
data class PlanSet(
    val id: Int,
    val title: String,
    val termFrom: String?,
    val termTo: String?
)
