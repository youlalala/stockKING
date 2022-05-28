package org.techtown.stockking.adapter


import android.graphics.Color
import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import org.techtown.stockking.R
import org.techtown.stockking.databinding.ToplistRecyclerviewBinding
import org.techtown.stockking.fragment.TopListFragment
import org.techtown.stockking.model.TopListRealtimeModel

class TopListRealtimeAdapter(
    private val stockTopList: List<TopListRealtimeModel>,
    val version: String,
    val context : TopListFragment,
    val onClickItem: (stockTopList: TopListRealtimeModel)-> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = RealtimeTopListViewHolder(ToplistRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as RealtimeTopListViewHolder).binding


        binding.itemId.text = (position+1).toString()
        val ticker = stockTopList[position].symbol
        binding.itemTicker.text = ticker
        binding.itemConame.text =stockTopList[position].name
        binding.itemClose.text = stockTopList[position].close

        if(version=="kr"){
            binding.itemClose.text = stockTopList[position].close + " ₩"
        }else{
            binding.itemClose.text = stockTopList[position].close + " $"
        }

        if(stockTopList[position].change_percent.substring(0,1)=="-"){
            binding.itemChangePercent.setTextColor(Color.BLUE)
            binding.itemChangeValue.setTextColor(Color.BLUE)
            binding.itemChangePercent.text = context.resources.getString(R.string.down)+stockTopList[position].change_percent.substring(1) + context.resources.getString(
                R.string.percent)
            if(version=="kr"){
                binding.itemChangeValue.text = stockTopList[position].change_value + context.resources.getString(R.string.won)
            }else{
                binding.itemChangeValue.text = stockTopList[position].change_value + context.resources.getString(R.string.dollar)
            }

        }else{
            binding.itemChangePercent.setTextColor(Color.RED)
            binding.itemChangeValue.setTextColor(Color.RED)
            binding.itemChangePercent.text = context.resources.getString(R.string.up)+stockTopList[position].change_percent.substring(1) + context.resources.getString(
                R.string.percent)
            if(version=="kr"){
                binding.itemChangeValue.text =  stockTopList[position].change_value + context.resources.getString(R.string.won)
            }else{
                binding.itemChangeValue.text =  stockTopList[position].change_value + context.resources.getString(R.string.dollar)
            }
        }

        //SVG string content
        val svgString = stockTopList[position].img

        //convert SVG string to an object of type SVG
        val svg = SVG.getFromString(svgString)

        //create a drawable from svg
        val drawable = PictureDrawable(svg.renderToPicture())

        //finally load the drawable with Glide.
        Glide.with(context)
            .load(drawable)
            .circleCrop()
            .into(binding.itemImg)

        holder.itemView.setOnClickListener{
            onClickItem.invoke(stockTopList[position])
        }
    }

    override fun getItemCount(): Int{
        return stockTopList.size
    }

    class RealtimeTopListViewHolder(val binding: ToplistRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)
}

