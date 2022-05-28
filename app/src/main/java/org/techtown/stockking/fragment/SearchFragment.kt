package org.techtown.stockking.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.postDelayed
import org.techtown.stockking.DetailActivity
import org.techtown.stockking.databinding.FragmentSearchBinding
import org.techtown.stockking.adapter.SearchListAdapter
import org.techtown.stockking.network.ApiWrapper

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
        getAllCompanyVersion("kr")

        binding.filterKrBtn.setOnClickListener {
            binding.filterKrBtn.isSelected = true
            binding.filterEnBtn.isSelected = false
            binding.filterTickerBtn.isSelected = false
            getAllCompanyVersion("kr")
        }

        binding.filterEnBtn.setOnClickListener {
            binding.filterKrBtn.isSelected = false
            binding.filterEnBtn.isSelected = true
            binding.filterTickerBtn.isSelected = false
            getAllCompanyVersion("en")
        }

        binding.filterTickerBtn.setOnClickListener {
            binding.filterKrBtn.isSelected = false
            binding.filterEnBtn.isSelected = false
            binding.filterTickerBtn.isSelected = true
            getAllCompanyVersion("kr")
        }


        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            //확인 누르면
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            //글자 칠때 마다 변함
            override fun onQueryTextChange(newText: String): Boolean {
                val handler = Handler()
                handler.postDelayed(500){
                    if(binding.searchBar.query.isNullOrEmpty()){
                        if(binding.filterEnBtn.isSelected){
                            getAllCompanyVersion("en")
                        }else{
                            getAllCompanyVersion("kr")
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
                }


                return true
            }
        })
        return binding.root
    }
    fun getAllCompanyVersion(version:String){
        ApiWrapper.getAllCompany() { it ->
            adapter= SearchListAdapter(it,version,this,onClickItem = {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("ticker",it.symbol)
                startActivity(intent)
            })
            binding.searchRecyclerView.adapter=adapter
        }
    }
}