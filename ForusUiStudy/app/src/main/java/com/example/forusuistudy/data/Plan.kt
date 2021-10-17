package com.example.forusuistudy.data


data class Plan(
    val id: Int,
    val title: String,
    val termFrom: String?,
    val termTo: String?,
    val url: String,
    val tagId: Int,
    val progress: Int
)