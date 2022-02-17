package org.techtown.stockking.module.search_page

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import org.techtown.stockking.R
import org.techtown.stockking.databinding.FragmentSearchBinding
import org.techtown.stockking.module.account_page.SettingActivity
import org.techtown.stockking.module.common.DetailActivity
import java.lang.StringBuilder

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    private val list = listOf("tsla","aapl","goog")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

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
                return true
            }
        })

        return binding.root
    }

}