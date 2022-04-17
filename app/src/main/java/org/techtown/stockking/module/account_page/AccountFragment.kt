package org.techtown.stockking.module.account_page

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAccountBinding.inflate(inflater, container, false)

        binding.settingTv.setOnClickListener {
            val intent = Intent(context,SettingActivity::class.java)
            startActivity(intent)
        }
        binding.userNameTv.text=MySharedPreferences.getUserName(requireContext())+" 님"


        return binding.root

    }

}