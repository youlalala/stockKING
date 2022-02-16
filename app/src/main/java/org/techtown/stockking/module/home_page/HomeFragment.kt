package org.techtown.stockking.module.home_page

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.techtown.stockking.R
import org.techtown.stockking.databinding.FragmentAccountBinding
import org.techtown.stockking.databinding.FragmentHomeBinding
import org.techtown.stockking.model.StockTopList
import org.techtown.stockking.module.account_page.SettingActivity
import org.techtown.stockking.module.common.DetailActivity
import org.techtown.stockking.network.ApiWrapper


class HomeFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.textVersion.setOnClickListener {
            if(binding.textVersion.text=="US"){
                binding.textVersion.text="KR"
                binding.textSwitch.isChecked= false
            }else{
                binding.textVersion.text="US"
                binding.textSwitch.isChecked= true
            }
        }
        binding.textSwitch.setOnClickListener{
            if(binding.textSwitch.isChecked){
                binding.textVersion.text="US"
            }else{
                binding.textVersion.text="KR"
            }
        }
        binding.detailTv.text = "실시간으로 사람들이 많이 검색하고 있는 주식 순위"
        binding.realtimeBtn.isSelected = true
        binding.updownBtn.isSelected = false
        binding.transactionBtn.isSelected = false

        binding.realtimeBtn.setOnClickListener {
            binding.detailTv.text = "실시간으로 사람들이 많이 검색하고 있는 주식 순위"
            binding.realtimeBtn.isSelected = true
            binding.updownBtn.isSelected = false
            binding.transactionBtn.isSelected = false
        }
        binding.updownBtn.setOnClickListener {
            binding.detailTv.text = "하룻동안 등락이 높은 주식 순위"
            binding.realtimeBtn.isSelected = false
            binding.updownBtn.isSelected = true
            binding.transactionBtn.isSelected = false
        }
        binding.transactionBtn.setOnClickListener {
            binding.detailTv.text = "하룻동안 가장 거래가 많았던 주식 순위"
            binding.realtimeBtn.isSelected = false
            binding.updownBtn.isSelected = false
            binding.transactionBtn.isSelected = true
        }

        //임시
        binding.tsla.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("ticker","tsla")
            startActivity(intent)
        }
        binding.aapl.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("ticker","aapl")
            startActivity(intent)
        }
        binding.goog.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("ticker","goog")
            startActivity(intent)
        }


        ApiWrapper.getStockTopList() { it ->
            binding.recyclerView.adapter=GeneralTopListAdapter(it,onClickItem = {

                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("ticker",it.title)
                intent.putExtra("coName",it.company)
                intent.putExtra("price",it.price)
                intent.putExtra("percent",it.percent)
                startActivity(intent)
            })

        }
        return binding.root
    }

}



