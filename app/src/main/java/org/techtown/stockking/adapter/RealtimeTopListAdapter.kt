package org.techtown.stockking.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.databinding.ToplistRecyclerviewBinding
import org.techtown.stockking.model.StockTopList

class RealtimeTopListAdapter(
    private val stockTopList: List<StockTopList>,
    val version:String,
    val onClickItem: (stockTopList: StockTopList)-> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = RealtimeTopListViewHolder(ToplistRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as RealtimeTopListViewHolder).binding


        binding.itemId.text = (position+1).toString()
        val ticker = stockTopList[position].title
        binding.itemTicker.text = ticker
        binding.itemConame.text =stockTopList[position].company
        binding.itemPrice.text = stockTopList[position].price

        if(stockTopList[position].percent.substring(0,1)=="-"){
            binding.itemPercent.setTextColor(Color.BLUE)
        }else{
            binding.itemPercent.setTextColor(Color.RED)
        }
        binding.itemPercent.text = stockTopList[position].percent

        holder.itemView.setOnClickListener{
            onClickItem.invoke(stockTopList[position])
        }
        holder.itemView.setOnClickListener{
            onClickItem.invoke(stockTopList[position])
        }
    }

    override fun getItemCount(): Int{
        return stockTopList.size
    }

    class RealtimeTopListViewHolder(val binding: ToplistRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)
}

