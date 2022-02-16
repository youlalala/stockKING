package org.techtown.stockking.module.home_page


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.databinding.ItemRecyclerviewBinding
import org.techtown.stockking.model.StockTopList

class GeneralTopListAdapter(
    private val stockTopList: List<StockTopList>,
    val onClickItem: (stockTopList: StockTopList)-> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return stockTopList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = ViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as ViewHolder).binding

        binding.itemId.text = (position+1).toString()
        binding.itemTicker.text = stockTopList[position].title
        binding.itemConame.text =stockTopList[position].company
        binding.itemPrice.text = stockTopList[position].price

        if(stockTopList[position].percent.substring(0,1)=="-"){
            binding.itemPercent.setTextColor(Color.RED)
        }else{
            binding.itemPercent.setTextColor(Color.BLUE)
        }
        binding.itemPercent.text = stockTopList[position].percent

        holder.itemView.setOnClickListener{
            onClickItem.invoke(stockTopList[position])
        }
    }
}

