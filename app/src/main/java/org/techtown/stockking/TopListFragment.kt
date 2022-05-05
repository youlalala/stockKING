package org.techtown.stockking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.techtown.stockking.adapter.RealtimeTopListAdapter
import org.techtown.stockking.databinding.FragmentToplistBinding

import org.techtown.stockking.network.ApiWrapper


class TopListFragment : Fragment(){

    lateinit var binding: FragmentToplistBinding

    //viewmodel
    //private val viewModel: RealTimeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToplistBinding.inflate(inflater, container, false)

        //val realtimeAdapter = RealtimeTopListAdapter()
        //binding.recyclerView.adapter=realtimeAdapter
        //subscribeUi(realtimeAdapter)


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

        topList("us")
        binding.textSwitch.setOnClickListener{
            if(binding.textSwitch.isChecked){
                binding.textVersion.text="KR"
                topList("kr")
            }else{
                binding.textVersion.text="US"
                topList("us")

            }
        }
        return binding.root
    }

    fun topList(version: String){
        ApiWrapper.getStockTopList() { it ->
            Log.i("la",it.toString())
            binding.recyclerView.adapter= RealtimeTopListAdapter(it,version,onClickItem = {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("ticker",it.title)
                intent.putExtra("percent",it.percent)
                intent.putExtra("price",it.price)
                startActivity(intent)
            })
        }

    }

//    private fun subscribeUi(adapter: RealtimeTopListAdapter){
//        viewModel.stocks.observe(viewLifecycleOwner){ stocks ->
//            realtimeAdapter.submitList(stocks)
//        }
//    }
}



