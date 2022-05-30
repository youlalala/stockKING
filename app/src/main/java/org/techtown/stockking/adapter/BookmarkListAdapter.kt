package org.techtown.stockking.adapter

import android.os.VibrationEffect
import android.os.VibrationEffect.EFFECT_CLICK
import android.R.attr.data
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.PictureDrawable
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import org.techtown.stockking.R
import org.techtown.stockking.databinding.BookmarkRecyclerviewBinding
import org.techtown.stockking.fragment.BookmarkFragment
import org.techtown.stockking.model.BookmarkListModel


class BookmarkListAdapter(
    private val bookmarkList: List<BookmarkListModel>,
    val context: BookmarkFragment,
    val onClickItem: (bookmarkList: BookmarkListModel)-> Unit,
    val onClickStar: (bookmarkList: BookmarkListModel)-> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int{
        return bookmarkList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        val binding = BookmarkRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as BookmarkViewHolder).binding

        val ticker = bookmarkList[position].symbol
        binding.itemTicker.text = ticker
        binding.itemConame.text = bookmarkList[position].name_kr
        binding.itemClose.text =  bookmarkList[position].close + context.resources.getString(R.string.dollar)

        if(bookmarkList[position].change_percent.substring(0,1)=="-"){
            binding.itemChangePercent.setTextColor(Color.BLUE)
            binding.itemChangeValue.setTextColor(Color.BLUE)
            binding.itemChangePercent.text = context.resources.getString(R.string.down)+bookmarkList[position].change_percent.substring(1) + context.resources.getString(
                R.string.percent)
            binding.itemChangeValue.text = bookmarkList[position].change_value + context.resources.getString(
                R.string.dollar)
        }else{
            binding.itemChangePercent.setTextColor(Color.RED)
            binding.itemChangeValue.setTextColor(Color.RED)
            binding.itemChangePercent.text = context.resources.getString(R.string.up)+bookmarkList[position].change_percent.substring(1) + context.resources.getString(
                R.string.percent)
            binding.itemChangeValue.text = bookmarkList[position].change_value + context.resources.getString(R.string.dollar)
        }

        //SVG string content
        val svgString = bookmarkList[position].img

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
            onClickItem.invoke(bookmarkList[position])
        }



        binding.star.setOnClickListener{
            onClickStar.invoke(bookmarkList[position])
        }

    }

    class BookmarkViewHolder(val binding: BookmarkRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)
}

