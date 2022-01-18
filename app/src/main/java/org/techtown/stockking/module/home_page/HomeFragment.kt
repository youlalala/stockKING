package org.techtown.stockking.module.home_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.techtown.stockking.databinding.FragmentHomeBinding
import org.techtown.stockking.model.StockTopList
import org.techtown.stockking.network.ApiWrapper


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding: FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        val switch = binding.textSwitch
        val version = binding.textVersion
        val btn1 = binding.realtimeBtn
        val btn2 = binding.updownBtn
        val btn3 = binding.transactionBtn
        val detail = binding.detailTv

        detail.text = "실시간으로 사람들이 많이 검색하고 있는 주식 순위"

        switch.setOnClickListener{
            if(switch.isChecked){
                version.text="US"
            }else{
                version.text="KR"
            }
        }

        binding.textVersion.setOnClickListener{
            if(version.text=="US"){
                version.text="KR"
                switch.isChecked= false
            }else{
                version.text="US"
                switch.isChecked= true
            }
        }

        btn1.setOnClickListener {
            detail.text = "실시간으로 사람들이 많이 검색하고 있는 주식 순위"
        }
        btn2.setOnClickListener {
            detail.text = "하룻동안 등락이 높은 주식 순위"
        }
        btn3.setOnClickListener {
            detail.text = "하룻동안 가장 거래가 많았던 주식 순위"
        }

        ApiWrapper.getStockTopList() {
            val adapter=dataToList(it)
            binding.recyclerView.adapter=adapter
            adapter.setItemClickListener( object : MyAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int) {
                    Log.i("SSS", "$position 번 리스트 선택")
                }
            })
        }
        return binding.root
    }

    private fun dataToList(symbolList: List<StockTopList>) : MyAdapter {
        val dataSymbol = mutableListOf<String>()
        val dataName = mutableListOf<String>()
        val dataPrice = mutableListOf<String>()
        val dataPercent = mutableListOf<String>()
        symbolList.forEach { element ->
            dataSymbol.add(element.title)
            dataName.add(element.company)
            dataPrice.add(element.price)
            dataPercent.add(element.percent)
        }
        return MyAdapter(dataSymbol,dataName,dataPrice,dataPercent)
    }

}

