package org.techtown.stockking.module.home_page


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.databinding.ItemRecyclerviewBinding
import org.techtown.stockking.model.StockTopList
import org.techtown.stockking.network.ApiWrapper

class GeneralTopListAdapter(
    private val stockTopList: List<StockTopList>,
    val version:String,
    val onClickItem: (stockTopList: StockTopList)-> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return stockTopList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = GeneralTopViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as GeneralTopViewHolder).binding

        binding.itemId.text = (position+1).toString()
        val ticker = stockTopList[position].title
        binding.itemTicker.text = ticker

        if(version=="us")
            binding.itemConame.text =stockTopList[position].company
        else{
            ApiWrapper.getCompanyInfo(ticker){ it->
                binding.itemConame.text = it[0].kr_name
            }
        }


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

