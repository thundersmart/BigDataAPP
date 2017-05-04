package com.example.pyj;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;

import com.example.fragment.Fragmentlink;
import com.example.fragment.Fragmentmain;
import com.example.fragment.Fragmentmsg;

import java.util.ArrayList;
import java.util.List;

import static com.example.Global.Titles;


/**
 * tab以及page内容
 */

public class PageActivity extends FragmentPagerAdapter {
    private FloatingActionButton fab;

    //构造函数
    public PageActivity(FragmentManager fragmentManager, FloatingActionButton fab) {
        super(fragmentManager);
        this.fab = fab;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            Fragmentmain fragmentmain = new Fragmentmain();
            fragmentmain.getFloatingActionButton(fab);
            return fragmentmain;
        } else if (position == 1) {
            return new Fragmentlink();
        } else if (position == 2) {
            return new Fragmentmsg();
        }
        return new Fragmentmain();
    }

    @Override
    public int getCount() {
        return Titles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

}
