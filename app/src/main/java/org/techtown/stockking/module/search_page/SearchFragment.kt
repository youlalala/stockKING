package org.techtown.stockking.module.search_page

import android.content.Intent
import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import org.techtown.stockking.databinding.FragmentSearchBinding
import org.techtown.stockking.module.common.detail_page.DetailActivity
import org.techtown.stockking.network.ApiWrapper

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    val symbolList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        getSymbol()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            //확인 누르면
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("ticker",query)
                startActivity(intent)
                return false
            }

            //글자 칠때 마다 변함
            override fun onQueryTextChange(newText: String): Boolean {
                search(newText)
                return true
            }
        })
        return binding.root
    }

    private fun search(query: String){
        var symbol : String
        val sb = StringBuilder()
        ApiWrapper.getStockTopList {
            for(i in it.indices){
                symbol = it[i].title
                if(symbol.toLowerCase().contains(query.toLowerCase())){
                    sb.append(symbol)
                    if(i!=it.size-1)
                        sb.append("\n")
                }

            }
            binding.resultView.text=sb.toString()
        }
    }


    private fun getSymbol(){
        var symbol : String
        val sb = StringBuilder()
        ApiWrapper.getStockTopList {
            for(i in it.indices){
                symbol = it[i].title
                sb.append(symbol)
                if(i!=it.size-1)
                    sb.append("\n")
            }
            binding.resultView.text=sb.toString()
        }
    }


}