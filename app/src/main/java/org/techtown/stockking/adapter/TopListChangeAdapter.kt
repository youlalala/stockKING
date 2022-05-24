package org.techtown.stockking.adapter

import android.graphics.Color
import android.graphics.drawable.PictureDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import org.techtown.stockking.databinding.ToplistChangeRecyclerviewBinding
import org.techtown.stockking.fragment.SearchFragment
import org.techtown.stockking.fragment.TopListFragment
import org.techtown.stockking.model.TopListChangeModel

class TopListChangeAdapter (
    private val stockTopList: List<TopListChangeModel>,
    val context : TopListFragment,
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
        binding.itemSymbol.text = symbol
        binding.itemName.text =stockTopList[position].name

        binding.itemChangeValue.text = stockTopList[position].change_value
        if(stockTopList[position].change_percent.substring(0,1)=="-"){
            binding.itemChangePercent.setTextColor(Color.BLUE)
        }else{
            binding.itemChangePercent.setTextColor(Color.RED)
        }
        binding.itemChangePercent.text = stockTopList[position].change_percent

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

    override fun getItemCount(): Int {
        return stockTopList.size
    }

    class TopListChangeViewHolder(val binding: ToplistChangeRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}