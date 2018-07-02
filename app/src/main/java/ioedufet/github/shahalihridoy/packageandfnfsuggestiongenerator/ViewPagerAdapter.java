package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> list = new ArrayList<Fragment>();
    List<String> nameList = new ArrayList<String>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return nameList.get(position);
    }

    public void addFragment(Fragment f,String name){
        list.add(f);
        nameList.add(name);
    }
}
