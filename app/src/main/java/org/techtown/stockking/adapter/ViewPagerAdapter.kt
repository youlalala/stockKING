package org.techtown.stockking.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.techtown.stockking.fragment.BookmarkFragment
import org.techtown.stockking.fragment.SearchFragment
import org.techtown.stockking.fragment.TopListFragment

class ViewPagerAdapter (fragment : FragmentActivity) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TopListFragment()
            1 -> BookmarkFragment()
            else -> SearchFragment()
        }
    }
}