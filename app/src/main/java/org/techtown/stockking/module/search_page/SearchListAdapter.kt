package org.techtown.stockking.module.search_page


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.databinding.SearchRecyclerviewBinding
import org.techtown.stockking.model.StockTopList
import android.widget.Filter
import org.techtown.stockking.SearchFragment
import kotlin.collections.ArrayList

class SearchListAdapter(
    val stockTopList: List<StockTopList>,
    val context : SearchFragment,
    val onClickItem: (stockTopList: StockTopList)-> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var filterList: List<StockTopList>

    init{
        this.filterList = stockTopList
    }

    override fun getItemCount(): Int{
        return filterList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = SearchViewHolder(SearchRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as SearchViewHolder).binding

        val ticker = filterList[position].title
        binding.itemTicker.text = ticker


        binding.itemConame.text =filterList[position].company

//        //SVG string content
//        val svgString = filterList[position].img
//
//        //convert SVG string to an object of type SVG
//        val svg = SVG.getFromString(svgString)
//
//        //create a drawable from svg
//        val drawable = PictureDrawable(svg.renderToPicture())
//
//        //finally load the drawable with Glide.
//        Glide.with(context)
//            .load(drawable)
//            .into(binding.testImg)


        holder.itemView.setOnClickListener{
            onClickItem.invoke(filterList[position])
        }
    }


    fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                filterList = if (charString.isEmpty()) {
                    stockTopList
                } else {
                    val filteredList = ArrayList<StockTopList>()
                    if (stockTopList != null) {
                        for (stock in stockTopList) {
                            //검색 종류
                            if(stock.title.toLowerCase().contains(charString.toLowerCase())
                            or stock.company.toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(stock);
                            }
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filterList  = results.values as ArrayList<StockTopList>
                notifyDataSetChanged()
            }
        }
    }

}

