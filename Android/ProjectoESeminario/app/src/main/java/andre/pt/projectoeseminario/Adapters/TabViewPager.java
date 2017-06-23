package andre.pt.projectoeseminario.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import andre.pt.projectoeseminario.Fragments.HistoryFragment;
import andre.pt.projectoeseminario.Fragments.ParentFragment;
import andre.pt.projectoeseminario.Fragments.PreferencesFragment;
import andre.pt.projectoeseminario.Interfaces.SettingsActions;

/*
* TabViewPager: Handles all the login when we swipe left/right on the settings activity
* */
public class TabViewPager extends FragmentStatePagerAdapter {
    private int mTabCount;
    private ParentFragment fragments[] = {new PreferencesFragment(), new HistoryFragment()};
    private SettingsActions ctx;

    public TabViewPager(FragmentManager fm, int tabCount, SettingsActions ctx) {
        super(fm);
        mTabCount = tabCount;
        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        if (position > fragments.length)
            throw new RuntimeException("TabViewPager is trying to access a invalid position: " + position);

        return fragments[position];
    }

    @Override
    public int getCount() {
        return mTabCount;
    }
}