package org.techtown.stockking.module.search_page

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import org.techtown.stockking.databinding.FragmentSearchBinding
import org.techtown.stockking.model.StockTopList
import org.techtown.stockking.module.common.detail_page.DetailActivity
import org.techtown.stockking.module.home_page.GeneralTopListAdapter
import org.techtown.stockking.network.ApiWrapper
import java.util.Locale.filter

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    var adapter: SearchListAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        ApiWrapper.getStockTopList() { it ->
            adapter= SearchListAdapter(it,this,onClickItem = {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("ticker",it.title)
                intent.putExtra("percent",it.percent)
                startActivity(intent)
            })
            binding.searchRecyclerView.adapter=adapter
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            //확인 누르면
            override fun onQueryTextSubmit(query: String): Boolean {
                //filter(query)
                return false
            }

            //글자 칠때 마다 변함
            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.getFilter()?.filter(newText)
                return true
            }
        })
        return binding.root
    }

}