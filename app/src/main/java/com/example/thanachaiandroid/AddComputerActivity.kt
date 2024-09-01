package com.example.thanachaiandroid

import com.example.thanachaiandroid.ApiService
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class AddComputerActivity : AppCompatActivity() {

    private lateinit var brandNameEditText: EditText
    private lateinit var modelNameEditText: EditText
    private lateinit var serialNumberEditText: EditText
    private lateinit var stockQuantityEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var cpuSpeedEditText: EditText
    private lateinit var memorySizeEditText: EditText
    private lateinit var hardDiskSizeEditText: EditText
    private lateinit var imageView: ImageView
    private lateinit var addButton: Button
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_computer)

        brandNameEditText = findViewById(R.id.brandNameEditText)
        modelNameEditText = findViewById(R.id.modelNameEditText)
        serialNumberEditText = findViewById(R.id.serialNumberEditText)
        stockQuantityEditText = findViewById(R.id.stockQuantityEditText)
        priceEditText = findViewById(R.id.priceEditText)
        cpuSpeedEditText = findViewById(R.id.cpuSpeedEditText)
        memorySizeEditText = findViewById(R.id.memorySizeEditText)
        hardDiskSizeEditText = findViewById(R.id.hardDiskSizeEditText)
        imageView = findViewById(R.id.imageView)
        addButton = findViewById(R.id.addButton)

        checkStoragePermission()

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            startActivityForResult(intent, 1)
        }

        addButton.setOnClickListener {
            addComputer()
        }
    }

    private fun checkStoragePermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 1001)
        }
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Storage permission granted")
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            imageView.setImageURI(selectedImageUri)

            val imagePath = getRealPathFromURI(selectedImageUri!!)
            if (imagePath != null) {
                Log.d("ImagePath", "Image Path: $imagePath")
                imageFile = File(imagePath)
            } else {
                Log.e("ImagePath", "Failed to get image path")
                Toast.makeText(this, "Failed to get image path", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                path = it.getString(columnIndex)
            }
        }
        return path
    }

    private fun addComputer() {
        val brandNameString = brandNameEditText.text.toString()
        val modelNameString = modelNameEditText.text.toString()
        val serialNumberString = serialNumberEditText.text.toString()
        val stockQuantityString = stockQuantityEditText.text.toString()
        val priceString = priceEditText.text.toString()
        val cpuSpeedString = cpuSpeedEditText.text.toString()
        val memorySizeString = memorySizeEditText.text.toString()
        val hardDiskSizeString = hardDiskSizeEditText.text.toString()

        val brandName = brandNameString.toRequestBody("text/plain".toMediaTypeOrNull())
        val modelName = modelNameString.toRequestBody("text/plain".toMediaTypeOrNull())
        val serialNumber = serialNumberString.toRequestBody("text/plain".toMediaTypeOrNull())
        val stockQuantity = stockQuantityString.toRequestBody("text/plain".toMediaTypeOrNull())
        val price = priceString.toRequestBody("text/plain".toMediaTypeOrNull())
        val cpuSpeed = cpuSpeedString.toRequestBody("text/plain".toMediaTypeOrNull())
        val memorySize = memorySizeString.toRequestBody("text/plain".toMediaTypeOrNull())
        val hardDiskSize = hardDiskSizeString.toRequestBody("text/plain".toMediaTypeOrNull())

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.70:4000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build())
            .build()

        val service = retrofit.create(ApiService::class.java)

        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }

        service.addComputer(
            brandName,
            modelName,
            serialNumber,
            stockQuantity,
            price,
            cpuSpeed,
            memorySize,
            hardDiskSize,
            imagePart
        ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val resultIntent = Intent().apply {
                        putExtra("brand_name", brandNameString)
                        putExtra("model_name", modelNameString)
                        putExtra("serial_number", serialNumberString)
                        putExtra("stock_quantity", stockQuantityString.toIntOrNull() ?: 0)
                        putExtra("price", priceString.toDoubleOrNull() ?: 0.0)
                        putExtra("cpu_speed", cpuSpeedString)
                        putExtra("memory_size", memorySizeString)
                        putExtra("hard_disk_size", hardDiskSizeString)
                        putExtra("image_url", imageFile?.absolutePath ?: "")
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                } else {
                    Log.e("AddComputer", "Failed to add computer: ${response.code()} ${response.message()}")
                    Toast.makeText(this@AddComputerActivity, "Failed to add computer: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("AddComputer", "Failed to add computer", t)
                Toast.makeText(this@AddComputerActivity, "Failed to add computer: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
