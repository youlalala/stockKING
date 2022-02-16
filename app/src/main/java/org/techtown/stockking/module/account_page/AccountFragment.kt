package org.techtown.stockking.module.account_page

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.techtown.stockking.R
import org.techtown.stockking.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        binding.settingTv.setOnClickListener {
            val intent = Intent(context,SettingActivity::class.java)
            startActivity(intent)
        }

        return binding.root

    }

}