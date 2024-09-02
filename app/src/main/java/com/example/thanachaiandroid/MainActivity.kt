package com.example.thanachaiandroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var computerAdapter: ComputerAdapter
    private val computerList = mutableListOf<Computer>()

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

                val newComputer = Computer(
                    id = computerList.size + 1, // Just a placeholder ID
                    brand_name = brandName,
                    model_name = modelName,
                    serial_number = serialNumber,
                    stock_quantity = stockQuantity,
                    price = price,
                    cpu_speed = cpuSpeed,
                    memory_size = memorySize,
                    hard_disk_size = hardDiskSize,
                    image_url = imageUrl
                )
                computerList.add(newComputer)
                computerAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        computerAdapter = ComputerAdapter(computerList)
        recyclerView.adapter = computerAdapter

        val addComputerButton: Button = findViewById(R.id.addComputerButton)
        addComputerButton.setOnClickListener {
            val intent = Intent(this, AddComputerActivity::class.java)
            addComputerActivityResultLauncher.launch(intent)
        }
    }
}
