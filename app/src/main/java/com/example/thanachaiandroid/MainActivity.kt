package com.example.thanachaiandroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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

    private val addComputerActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.let {
                val brandName = it.getStringExtra("brand_name") ?: "N/A"
                val modelName = it.getStringExtra("model_name") ?: "N/A"
                val serialNumber = it.getStringExtra("serial_number") ?: "N/A"
                val stockQuantity = it.getIntExtra("stock_quantity", 0)
                val price = it.getDoubleExtra("price", 0.0)
                val cpuSpeed = it.getStringExtra("cpu_speed") ?: "N/A"
                val memorySize = it.getStringExtra("memory_size") ?: "N/A"
                val hardDiskSize = it.getStringExtra("hard_disk_size") ?: "N/A"
                val imageUrl = it.getStringExtra("image_url") ?: ""

                brandNameTextView.text = "Brand: $brandName"
                modelNameTextView.text = "Model: $modelName"
                serialNumberTextView.text = "Serial Number: $serialNumber"
                stockQuantityTextView.text = "Stock Quantity: $stockQuantity"
                priceTextView.text = "Price: $price"
                cpuSpeedTextView.text = "CPU Speed: $cpuSpeed"
                memorySizeTextView.text = "Memory Size: $memorySize"
                hardDiskSizeTextView.text = "Hard Disk Size: $hardDiskSize"

                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(computerImageView)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        openAddComputerActivityButton.setOnClickListener {
            val intent = Intent(this, AddComputerActivity::class.java)
            addComputerActivityResultLauncher.launch(intent)
        }
    }
}
