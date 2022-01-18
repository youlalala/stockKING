package org.techtown.stockking

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.techtown.stockking.module.account_page.AccountFragment
import org.techtown.stockking.module.search_page.SearchFragment
import org.techtown.stockking.module.home_page.HomeFragment

class ViewPagerAdapter (fragment : FragmentActivity) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> AccountFragment()
            else -> SearchFragment()
        }
    }
}