package org.techtown.stockking.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import org.techtown.stockking.DetailActivity
import org.techtown.stockking.R
import org.techtown.stockking.adapter.RealtimeTopListAdapter
import org.techtown.stockking.databinding.FragmentSearchBinding
import org.techtown.stockking.adapter.SearchListAdapter
import org.techtown.stockking.network.ApiWrapper
import java.util.regex.Pattern

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    var adapter: SearchListAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.filterKrBtn.isSelected = true
        binding.filterEnBtn.isSelected = false
        binding.filterTickerBtn.isSelected = false

        binding.filterKrBtn.setOnClickListener {
            binding.filterKrBtn.isSelected = true
            binding.filterEnBtn.isSelected = false
            binding.filterTickerBtn.isSelected = false
        }

        binding.filterEnBtn.setOnClickListener {
            binding.filterKrBtn.isSelected = false
            binding.filterEnBtn.isSelected = true
            binding.filterTickerBtn.isSelected = false
        }

        binding.filterTickerBtn.setOnClickListener {
            binding.filterKrBtn.isSelected = false
            binding.filterEnBtn.isSelected = false
            binding.filterTickerBtn.isSelected = true
        }

        ApiWrapper.getAllCompany() { it ->
            adapter= SearchListAdapter(it,"kr",this,onClickItem = {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("ticker",it.symbol)
                startActivity(intent)
            })
            binding.searchRecyclerView.adapter=adapter
        }




        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            //확인 누르면
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            //글자 칠때 마다 변함
            override fun onQueryTextChange(newText: String): Boolean {
                if(binding.searchBar.query.isNullOrEmpty()){
                    ApiWrapper.getAllCompany() { it ->
                        adapter= SearchListAdapter(it,"kr",this@SearchFragment,onClickItem = {
                            val intent = Intent(context, DetailActivity::class.java)
                            intent.putExtra("ticker",it.symbol)
                            startActivity(intent)
                        })
                        binding.searchRecyclerView.adapter=adapter
                    }
                }
                if(binding.filterKrBtn.isSelected){
                    ApiWrapper.getSearch("kr",newText) { it ->
                        adapter= SearchListAdapter(it,"kr",this@SearchFragment,onClickItem = {
                            val intent = Intent(context, DetailActivity::class.java)
                            intent.putExtra("ticker",it.symbol)
                            startActivity(intent)
                        })
                        binding.searchRecyclerView.adapter=adapter
                    }
                }else if(binding.filterEnBtn.isSelected){
                    ApiWrapper.getSearch("en",newText) { it ->
                        adapter= SearchListAdapter(it,"en",this@SearchFragment,onClickItem = {
                            val intent = Intent(context, DetailActivity::class.java)
                            intent.putExtra("ticker",it.symbol)
                            startActivity(intent)
                        })
                        binding.searchRecyclerView.adapter=adapter
                    }
                }else{
                    ApiWrapper.getSearch("symbol",newText) { it ->
                        adapter= SearchListAdapter(it,"kr",this@SearchFragment,onClickItem = {
                            val intent = Intent(context, DetailActivity::class.java)
                            intent.putExtra("ticker",it.symbol)
                            startActivity(intent)
                        })
                        binding.searchRecyclerView.adapter=adapter
                    }
                }

                return true
            }
        })
        return binding.root
    }

}