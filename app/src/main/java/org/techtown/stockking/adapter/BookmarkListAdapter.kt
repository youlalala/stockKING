package org.techtown.stockking.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import org.techtown.stockking.databinding.BookmarkRecyclerviewBinding
import org.techtown.stockking.model.BookMarkPersonalModel


class BookmarkListAdapter(
    private val bookmarkList: List<BookMarkPersonalModel>,
    //val context: Context,
    val onClickItem: (bookmarkList: BookMarkPersonalModel)-> Unit,
    val onClickStar: (bookmarkList: BookMarkPersonalModel)-> Unit
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


    class BookmarkViewHolder(val binding: BookmarkRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)
}

