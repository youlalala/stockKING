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
        val temp_context = this

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
            adapter= SearchListAdapter(it,this,onClickItem = {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("ticker",it.symbol)
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
                //adapter?.getFilter()?.filter(newText)
                if(binding.filterKrBtn.isSelected){
                    ApiWrapper.getSearch("kr",newText) { it ->
                        adapter= SearchListAdapter(it,temp_context,onClickItem = {
                            val intent = Intent(context, DetailActivity::class.java)
                            intent.putExtra("ticker",it.symbol)
                            startActivity(intent)
                        })
                        binding.searchRecyclerView.adapter=adapter
                    }
                }else if(binding.filterEnBtn.isSelected){
                    ApiWrapper.getSearch("en",newText) { it ->
                        adapter= SearchListAdapter(it,temp_context,onClickItem = {
                            val intent = Intent(context, DetailActivity::class.java)
                            intent.putExtra("ticker",it.symbol)
                            startActivity(intent)
                        })
                        binding.searchRecyclerView.adapter=adapter
                    }
                }else{
                    ApiWrapper.getSearch("symbol",newText) { it ->
                        adapter= SearchListAdapter(it,temp_context,onClickItem = {
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