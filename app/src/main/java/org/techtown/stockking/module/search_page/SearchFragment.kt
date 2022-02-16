package org.techtown.stockking.module.search_page

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import org.techtown.stockking.R
import org.techtown.stockking.databinding.FragmentSearchBinding
import org.techtown.stockking.module.account_page.SettingActivity

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)


        return binding.root
    }


}