package com.example.mynasatask.model

data class MarsListEntry(
    val full_name: String,
    val img_src: String,
    val number: Int,
    val camera_name: String,
    val rover_name: String,
    val landing_date: String,
    val launch_date: String,
    val earth_date: String
)