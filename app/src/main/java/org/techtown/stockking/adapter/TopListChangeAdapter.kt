package org.techtown.stockking.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.databinding.ToplistChangeRecyclerviewBinding
import org.techtown.stockking.model.TopListChangeModel

class TopListChangeAdapter (
    private val stockTopList: List<TopListChangeModel>,
    val onClickItem: (stockTopList: TopListChangeModel)-> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        TopListChangeViewHolder(
            ToplistChangeRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as TopListChangeViewHolder).binding


        binding.itemId.text = (position+1).toString()
        val symbol = stockTopList[position].symbol
        //Log.i("youla",stockTopList[1].toString())
        binding.itemSymbol.text = symbol
        binding.itemName.text =stockTopList[position].name
//        binding.itemChangeValue.text = stockTopList[position].change_value
//
//        if(stockTopList[position].change_percnet.substring(0,1)=="-"){
//            binding.itemChangePercent.setTextColor(Color.BLUE)
//        }else{
//            binding.itemChangePercent.setTextColor(Color.RED)
//        }
//        binding.itemChangePercent.text = stockTopList[position].change_percnet

        holder.itemView.setOnClickListener{
            onClickItem.invoke(stockTopList[position])
        }

    }

    override fun getItemCount(): Int {
        return stockTopList.size
    }

    class TopListChangeViewHolder(val binding: ToplistChangeRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}