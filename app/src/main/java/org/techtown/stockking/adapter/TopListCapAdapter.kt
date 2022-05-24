package org.techtown.stockking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.databinding.ToplistCapRecyclerviewBinding
import org.techtown.stockking.model.TopListCapModel

class TopListCapAdapter (
    private val stockTopList: List<TopListCapModel>,
    val version: String,
    val onClickItem: (stockTopList: TopListCapModel)-> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        TopListCapViewHolder(
            ToplistCapRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as TopListCapViewHolder).binding


        binding.itemId.text = (position+1).toString()
        val symbol = stockTopList[position].symbol
        //Log.i("youla",stockTopList[1].toString())
        binding.itemSymbol.text = symbol
        binding.itemName.text =stockTopList[position].name
        if(version=="kr"){
            binding.itemCap.text = stockTopList[position].cap + "원"
        }else{
            binding.itemCap.text = stockTopList[position].cap + "$"
        }


        holder.itemView.setOnClickListener{
            onClickItem.invoke(stockTopList[position])
        }

    }

    override fun getItemCount(): Int {
        return stockTopList.size
    }

    class TopListCapViewHolder(val binding: ToplistCapRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}