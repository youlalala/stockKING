package org.techtown.stockking.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.techtown.stockking.DetailActivity
import org.techtown.stockking.SettingActivity
import org.techtown.stockking.common.AppLog
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.FragmentBookmarkBinding
import org.techtown.stockking.adapter.BookmarkListAdapter
import org.techtown.stockking.model.BookmarkModel
import org.techtown.stockking.network.ApiWrapperBookmark

class BookmarkFragment : Fragment() {

    lateinit var binding: FragmentBookmarkBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        binding.settingTv.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }
        binding.userNameTv.text=MySharedPreferences.getUserName(requireContext())+" 님의 즐겨찾기"
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        ApiWrapperBookmark.getBookmarkList(MySharedPreferences.getToken(requireContext())){
            if(it.code == "200"){
                binding.emptyTv.visibility=View.GONE
                binding.recyclerView.visibility=View.VISIBLE
                val adapter= BookmarkListAdapter(it.result.symbol,this,
                    onClickItem = {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra("ticker",it.symbol)
                        startActivity(intent)
                    }, onClickStar={
                        AppLog.i("Stock", "onClickStar"+"it:"+it)
                        Toast.makeText(context,it.symbol+"가(이) 삭제되었습니다.",Toast.LENGTH_SHORT).show()
                        ApiWrapperBookmark.deleteFavorite(
                            token = MySharedPreferences.getToken(requireContext()),
                            BookmarkModel(
                                symbol = it.symbol)
                        ) {
                            if (it) {
                                onStart()
                            }
                        }
                    }
                )
                binding.recyclerView.adapter = adapter
            }else{
                binding.emptyTv.visibility=View.VISIBLE
                binding.recyclerView.visibility=View.GONE
            }

        }
    }

}