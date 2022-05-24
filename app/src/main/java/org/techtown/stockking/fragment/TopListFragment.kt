package org.techtown.stockking.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.techtown.stockking.DetailActivity
import org.techtown.stockking.R
import org.techtown.stockking.adapter.RealtimeTopListAdapter
import org.techtown.stockking.adapter.TopListCapAdapter
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

//        binding.exchangeSwitch.showText = true
//        binding.exchangeSwitch.textOff = "$"
//        binding.exchangeSwitch.textOn = "₩"

        binding.sortSwitch.showText = true
        binding.sortSwitch.textOff = ""
        binding.sortSwitch.textOn = ""

        binding.buttonTitleTv.text = resources.getString(R.string.topList_btn1_title)
        binding.buttonDetailTv.text = resources.getString(R.string.topList_btn1_detail)
        binding.realtimeBtn.isSelected = true
        binding.updownBtn.isSelected = false
        binding.transactionBtn.isSelected = false

        //radio group
        binding.dolloarBtn.isSelected=true
        binding.wonBtn.isSelected=false


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

            //binding.exchangeSwitch.visibility = View.VISIBLE
            binding.sortSwitch.visibility = View.GONE


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

            //switch
            //binding.exchangeSwitch.visibility = View.GONE
            binding.sortSwitch.visibility = View.VISIBLE

            getTopList("change","desc")
            binding.sortSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                binding.progressBar.visibility=View.VISIBLE
                if(isChecked){
                    getTopList("change","asc")
                }else{
                    getTopList("change","desc")
                }
            }
        }
        binding.transactionBtn.setOnClickListener {
            binding.buttonTitleTv.text = resources.getString(R.string.topList_btn3_title)
            binding.buttonDetailTv.text = resources.getString(R.string.topList_btn3_detail)
            binding.realtimeBtn.isSelected = false
            binding.updownBtn.isSelected = false
            binding.transactionBtn.isSelected = true

            //switch
            //binding.exchangeSwitch.visibility = View.VISIBLE
            binding.sortSwitch.visibility = View.GONE

            getTopList("cap","en")

            binding.dolloarBtn.setOnClickListener{
                binding.dolloarBtn.isSelected=true
                binding.wonBtn.isSelected=false
                getTopList("cap","en")
            }
            binding.wonBtn.setOnClickListener {
                binding.dolloarBtn.isSelected=false
                binding.wonBtn.isSelected=true
                getTopList("cap","kr")
            }

//            binding.sortSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
//                if(isChecked){
//                    getTopList("cap","kr")
//                }else{
//                    getTopList("cap","en")
//                }
//            }
        }

        return binding.root
    }

    fun getTopList(filter1: String, filter2: String){
        if(filter1 == "change"){
            ApiWrapper.getTopListChange(filter2) { it ->
                binding.progressBar.visibility=View.GONE
                binding.recyclerView.adapter= TopListChangeAdapter(it,this,onClickItem = {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("ticker",it.symbol)
                    startActivity(intent)
                })
            }
        }else if(filter1 == "cap"){
            ApiWrapper.getTopListCap(filter2){ it ->
                binding.progressBar.visibility=View.GONE
                binding.recyclerView.adapter= TopListCapAdapter(it,filter2,this,onClickItem = {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("ticker",it.symbol)
                    startActivity(intent)
                })

            }
        }
    }

}



