package org.techtown.stockking.module.account_page

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.techtown.stockking.common.AppLog
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.FragmentAccountBinding
import org.techtown.stockking.model.BookmarkModel
import org.techtown.stockking.module.common.detail_page.DetailActivity
import org.techtown.stockking.network.ApiWrapper

class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.settingTv.setOnClickListener {
            val intent = Intent(context,SettingActivity::class.java)
            startActivity(intent)
        }
        binding.userNameTv.text=MySharedPreferences.getUserName(requireContext())+" 님"



        return binding.root

    }
    override fun onStart() {
        super.onStart()
        ApiWrapper.getBookmark(MySharedPreferences.getToken(requireContext())){
            binding.recyclerView.adapter=BookmarkListAdapter(it,onClickItem = {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("ticker",it.symbol)
                startActivity(intent)
            },
                onClickStar={
                    AppLog.i("Stock", "onClickStar")
//                ApiWrapper.postBookmark(
//                    BookmarkModel(
//                        token = MySharedPreferences.getToken(),
//                        request = "delete",
//                        symbol = it.symbol)
//                ){}
            })
        }
    }



}