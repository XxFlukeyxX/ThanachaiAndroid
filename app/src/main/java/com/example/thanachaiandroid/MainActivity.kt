package com.example.thanachaiandroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var brandNameTextView: TextView
    private lateinit var modelNameTextView: TextView
    private lateinit var serialNumberTextView: TextView
    private lateinit var stockQuantityTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var cpuSpeedTextView: TextView
    private lateinit var memorySizeTextView: TextView
    private lateinit var hardDiskSizeTextView: TextView
    private lateinit var computerImageView: ImageView
    private lateinit var openAddComputerActivityButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        brandNameTextView = findViewById(R.id.brandNameTextView)
        modelNameTextView = findViewById(R.id.modelNameTextView)
        serialNumberTextView = findViewById(R.id.serialNumberTextView)
        stockQuantityTextView = findViewById(R.id.stockQuantityTextView)
        priceTextView = findViewById(R.id.priceTextView)
        cpuSpeedTextView = findViewById(R.id.cpuSpeedTextView)
        memorySizeTextView = findViewById(R.id.memorySizeTextView)
        hardDiskSizeTextView = findViewById(R.id.hardDiskSizeTextView)
        computerImageView = findViewById(R.id.computerImageView)
        openAddComputerActivityButton = findViewById(R.id.openAddComputerActivityButton)

        // Handle the button click to open AddComputerActivity
        openAddComputerActivityButton.setOnClickListener {
            val intent = Intent(this, AddComputerActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            // Get the data returned from AddComputerActivity
            val brandName = data.getStringExtra("brand_name")
            val modelName = data.getStringExtra("model_name")
            val serialNumber = data.getStringExtra("serial_number")
            val stockQuantity = data.getIntExtra("stock_quantity", 0)
            val price = data.getDoubleExtra("price", 0.0)
            val cpuSpeed = data.getStringExtra("cpu_speed")
            val memorySize = data.getStringExtra("memory_size")
            val hardDiskSize = data.getStringExtra("hard_disk_size")
            val imageUrl = data.getStringExtra("image_url")

            // Display the received data in the UI
            brandNameTextView.text = "Brand: $brandName"
            modelNameTextView.text = "Model: $modelName"
            serialNumberTextView.text = "Serial Number: $serialNumber"
            stockQuantityTextView.text = "Stock Quantity: $stockQuantity"
            priceTextView.text = "Price: $price"
            cpuSpeedTextView.text = "CPU Speed: $cpuSpeed"
            memorySizeTextView.text = "Memory Size: $memorySize"
            hardDiskSizeTextView.text = "Hard Disk Size: $hardDiskSize"

            // Load image using Glide
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(computerImageView)
        }
    }
}
