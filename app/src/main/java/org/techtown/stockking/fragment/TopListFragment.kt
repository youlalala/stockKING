package org.techtown.stockking.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.techtown.stockking.DetailActivity
import org.techtown.stockking.R
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

        binding.buttonTitleTv.text = resources.getString(R.string.topList_btn1_title)
        binding.buttonDetailTv.text = resources.getString(R.string.topList_btn1_detail)
        binding.realtimeBtn.isSelected = true
        binding.updownBtn.isSelected = false
        binding.transactionBtn.isSelected = false

        binding.realtimeBtn.setOnClickListener {
            binding.buttonTitleTv.text = resources.getString(R.string.topList_btn1_title)
            binding.buttonDetailTv.text = resources.getString(R.string.topList_btn1_detail)
            binding.realtimeBtn.isSelected = true
            binding.updownBtn.isSelected = false
            binding.transactionBtn.isSelected = false
        }
        binding.updownBtn.setOnClickListener {
            binding.buttonTitleTv.text = resources.getString(R.string.topList_btn2_title)
            binding.buttonDetailTv.text = resources.getString(R.string.topList_btn2_detail)
            binding.realtimeBtn.isSelected = false
            binding.updownBtn.isSelected = true
            binding.transactionBtn.isSelected = false
        }
        binding.transactionBtn.setOnClickListener {
            binding.buttonTitleTv.text = resources.getString(R.string.topList_btn3_title)
            binding.buttonDetailTv.text = resources.getString(R.string.topList_btn3_detail)
            binding.realtimeBtn.isSelected = false
            binding.updownBtn.isSelected = false
            binding.transactionBtn.isSelected = true
        }

        topList("us")

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



