package com.example.thanachaiandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ComputerAdapter(private val computers: List<Computer>) :
    RecyclerView.Adapter<ComputerAdapter.ComputerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComputerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_computer, parent, false)
        return ComputerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComputerViewHolder, position: Int) {
        val computer = computers[position]
        holder.bind(computer)
    }

    override fun getItemCount(): Int = computers.size

    inner class ComputerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val brandNameTextView: TextView = itemView.findViewById(R.id.brandNameTextView)
        private val modelNameTextView: TextView = itemView.findViewById(R.id.modelNameTextView)
        private val serialNumberTextView: TextView = itemView.findViewById(R.id.serialNumberTextView)
        private val stockQuantityTextView: TextView = itemView.findViewById(R.id.stockQuantityTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val computerImageView: ImageView = itemView.findViewById(R.id.computerImageView)

        fun bind(computer: Computer) {
            brandNameTextView.text = "Brand: ${computer.brand_name}"
            modelNameTextView.text = "Model: ${computer.model_name}"
            serialNumberTextView.text = "Serial Number: ${computer.serial_number}"
            stockQuantityTextView.text = "Stock Quantity: ${computer.stock_quantity}"
            priceTextView.text = "Price: ${computer.price}"

            Glide.with(itemView.context)
                .load(computer.image_url)
                .placeholder(R.drawable.placeholder_image)
                .into(computerImageView)
        }
    }
}
