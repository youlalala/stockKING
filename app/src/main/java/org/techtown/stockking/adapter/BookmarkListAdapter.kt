package org.techtown.stockking.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.databinding.BookmarkRecyclerviewBinding
import org.techtown.stockking.model.BookmarkListModel


class BookmarkListAdapter(
    private val bookmarkList: List<BookmarkListModel>,
    //val context: Context,
    val onClickItem: (bookmarkList: BookmarkListModel)-> Unit,
    val onClickStar: (bookmarkList: BookmarkListModel)-> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int{
        return bookmarkList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = BookmarkViewHolder(BookmarkRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as BookmarkViewHolder).binding

        val ticker = bookmarkList[position].symbol
        binding.tickerTv.text = ticker

        holder.itemView.setOnClickListener{
            onClickItem.invoke(bookmarkList[position])
        }
        binding.star.setOnClickListener{
            onClickStar.invoke(bookmarkList[position])
        }
    }


    class BookmarkViewHolder(val binding: BookmarkRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)
}

