package org.techtown.stockking.adapter


import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.databinding.SearchRecyclerviewBinding
import org.techtown.stockking.model.StockTopList
import android.widget.Filter
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import org.techtown.stockking.fragment.SearchFragment
import org.techtown.stockking.model.SearchModel
import kotlin.collections.ArrayList

class SearchListAdapter(
    val searchList: List<SearchModel>,
    val context : SearchFragment,
    val onClickItem: (searchList: SearchModel)-> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var filterList: List<SearchModel>

    init{
        this.filterList = searchList
    }

    override fun getItemCount(): Int{
        return filterList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = SearchViewHolder(SearchRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as SearchViewHolder).binding

        val ticker = filterList[position].symbol
        binding.itemTicker.text = ticker


        binding.itemConame.text =filterList[position].name_en

        //SVG string content
        val svgString = filterList[position].img

        //convert SVG string to an object of type SVG
        val svg = SVG.getFromString(svgString)

        //create a drawable from svg
        val drawable = PictureDrawable(svg.renderToPicture())

        //finally load the drawable with Glide.
        Glide.with(context)
            .load(drawable)
            .circleCrop()
            .into(binding.testImg)




        holder.itemView.setOnClickListener{
            onClickItem.invoke(filterList[position])
        }
    }


    class SearchViewHolder(val binding: SearchRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

}

