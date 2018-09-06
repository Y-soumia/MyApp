package com.example.youbi.myapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.example.youbi.myapp.Fragments.Fragment_1;
import com.example.youbi.myapp.Fragments.Fragment_2;
import com.example.youbi.myapp.Fragments.Fragment_3;
import com.example.youbi.myapp.R;

public class PagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    private static int num_items = 3;
    private int tabIcons[] = {R.drawable.ic_home, R.drawable.ic_favorite, R.drawable.ic_ads};


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // Returns the number of pages
    @Override
    public int getCount() {
        return num_items;
    }

    // Returns the fragment to display
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Fragment_1();
            case 1:
                return new Fragment_2();
            case 2:
                return new Fragment_3();
        }
        return null;
    }

    // setting the tabs icons
    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }

    // Returns the page title
    /*@Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Annonces";
            case 1:
                return "Favorits";
            case 2:
                return "Mes annonces";
            default:
                return null;
        }
    }
    */
}
