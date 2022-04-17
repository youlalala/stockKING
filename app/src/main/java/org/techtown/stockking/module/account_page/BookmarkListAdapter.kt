package org.techtown.stockking.module.account_page


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.databinding.BookmarkRecyclerviewBinding
import org.techtown.stockking.model.BookMarkPersonalModel


class BookmarkListAdapter(
    private val bookmarkList: List<BookMarkPersonalModel>,
    val onClickItem: (bookmarkList: BookMarkPersonalModel)-> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

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
    }
}

