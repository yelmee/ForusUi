package com.example.forusuistudy.data


data class Plan(
    val id: Int,
    val title: String,
    val startDate: String?,
    val endDate: String?,
    val url: String,
    val tagId: Int,
    val progress: Int
)