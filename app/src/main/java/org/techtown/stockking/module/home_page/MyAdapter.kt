package org.techtown.stockking.module.home_page

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.DetailActivity
import org.techtown.stockking.databinding.ItemRecyclerviewBinding

class MyAdapter(val dataSymbol: MutableList<String>,
                val dataName: MutableList<String>, val dataPrice: MutableList<String>,
                val dataPercent: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return dataSymbol.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding

        binding.itemId.text = (position+1).toString()
        binding.itemName.text = dataName[position]
        binding.itemPrice.text = dataPrice[position]
        binding.itemPercent.text = dataPercent[position]

        holder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)

            intent.putExtra("name",dataName[position])
            intent.putExtra("symbol",dataSymbol[position])
            intent.putExtra("price",dataPrice[position])
            intent.putExtra("percent",dataPercent[position])
            startActivity(holder.itemView.context, intent, null)
        }

    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListner: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}