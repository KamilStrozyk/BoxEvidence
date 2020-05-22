package com.example.boxEvidence.activities.table.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


private val TAB_TITLES = arrayOf(
    "Locations",
    "Boxes",
    "Items"
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        when (position) {
            0 -> return LocationView()
            1 -> return PlaceholderFragment.newInstance(position + 1)
            2 -> return PlaceholderFragment.newInstance(position + 1)
        }
        return PlaceholderFragment.newInstance(position + 1)
    }



    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {

        return 3
    }
}