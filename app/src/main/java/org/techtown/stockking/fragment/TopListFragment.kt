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
import org.techtown.stockking.adapter.TopListChangeAdapter
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

        binding.moneySwitch.showText = true
        binding.moneySwitch.textOff = "$"
        binding.moneySwitch.textOn = "₩"

        binding.buttonTitleTv.text = resources.getString(R.string.topList_btn1_title)
        binding.buttonDetailTv.text = resources.getString(R.string.topList_btn1_detail)
        binding.realtimeBtn.isSelected = true
        binding.updownBtn.isSelected = false
        binding.transactionBtn.isSelected = false

        ApiWrapper.getTopListRealtime() { it ->
            Log.i("la",it.toString())
            binding.recyclerView.adapter= RealtimeTopListAdapter(it,onClickItem = {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("ticker",it.title)
                intent.putExtra("percent",it.percent)
                intent.putExtra("price",it.price)
                startActivity(intent)
            })
        }

        binding.realtimeBtn.setOnClickListener {
            binding.buttonTitleTv.text = resources.getString(R.string.topList_btn1_title)
            binding.buttonDetailTv.text = resources.getString(R.string.topList_btn1_detail)
            binding.realtimeBtn.isSelected = true
            binding.updownBtn.isSelected = false
            binding.transactionBtn.isSelected = false
            ApiWrapper.getTopListRealtime() { it ->
                Log.i("la",it.toString())
                binding.recyclerView.adapter= RealtimeTopListAdapter(it,onClickItem = {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("ticker",it.title)
                    intent.putExtra("percent",it.percent)
                    intent.putExtra("price",it.price)
                    startActivity(intent)
                })
            }
        }
        binding.updownBtn.setOnClickListener {
            binding.buttonTitleTv.text = resources.getString(R.string.topList_btn2_title)
            binding.buttonDetailTv.text = resources.getString(R.string.topList_btn2_detail)
            binding.realtimeBtn.isSelected = false
            binding.updownBtn.isSelected = true
            binding.transactionBtn.isSelected = false
            getTopList("change","asc")
            //getTopList("change","desc")
        }
        binding.transactionBtn.setOnClickListener {
            binding.buttonTitleTv.text = resources.getString(R.string.topList_btn3_title)
            binding.buttonDetailTv.text = resources.getString(R.string.topList_btn3_detail)
            binding.realtimeBtn.isSelected = false
            binding.updownBtn.isSelected = false
            binding.transactionBtn.isSelected = true
        }


        return binding.root
    }

    fun getTopList(filter1: String, filter2: String){
        if(filter1 == "change"){
            ApiWrapper.getTopListChange(filter2) { it ->
                Log.i("SSS",it.toString())
                binding.recyclerView.adapter= TopListChangeAdapter(it,onClickItem = {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("ticker",it.symbol)
                    startActivity(intent)
                })
            }
        }
    }

}



