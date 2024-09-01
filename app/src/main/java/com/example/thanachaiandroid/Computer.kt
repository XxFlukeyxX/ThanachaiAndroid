package com.example.thanachaiandroid

data class Computer(
    val id: Int,
    val brand_name: String,
    val model_name: String,
    val serial_number: String,
    val stock_quantity: Int,
    val price: Double,
    val cpu_speed: String,
    val memory_size: String,
    val hard_disk_size: String,
    val image_url: String
)
