package org.techtown.stockking.module.account_page


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.BookmarkRecyclerviewBinding
import org.techtown.stockking.model.BookMarkPersonalModel
import org.techtown.stockking.model.BookmarkModel
import org.techtown.stockking.network.ApiWrapper


class BookmarkListAdapter(
    private val bookmarkList: List<BookMarkPersonalModel>,
    //val context: Context,
    val onClickItem: (bookmarkList: BookMarkPersonalModel)-> Unit,
    val onClickStar: ()-> Unit
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
            onClickStar.invoke()
            //onClickStar.invoke(bookmarkList[position])
//
            //ApiWrapper.postBookmark(
//                    BookmarkModel(
//                        token = MySharedPreferences.getToken(context),
//                        request = "delete",
//                        symbol = ticker)
//                ){}
        }
    }
}

