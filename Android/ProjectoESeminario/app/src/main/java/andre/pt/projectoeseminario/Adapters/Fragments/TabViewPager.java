package andre.pt.projectoeseminario.Adapters.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import andre.pt.projectoeseminario.Fragments.HistoryFragment;
import andre.pt.projectoeseminario.Fragments.Interfaces.IHistory;
import andre.pt.projectoeseminario.Fragments.Interfaces.ParentFragment;
import andre.pt.projectoeseminario.Fragments.PreferencesFragment;
import andre.pt.projectoeseminario.Activities.Interfaces.SettingsActions;

/*
* TabViewPager: Handles all the login when we swipe left/right on the settings activity
* */
public class TabViewPager extends FragmentStatePagerAdapter implements IViewPager{
    private int mTabCount;
    private static final ParentFragment fragments[] = {new HistoryFragment(), new PreferencesFragment()};
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


    @Override
    public boolean isInDetailedView(int position) {
        if(position != 0)
            return false;

        return !((IHistory)fragments[position]).isInCategoriesView();
    }

    @Override
    public void returnToCategories() {
        ((IHistory)fragments[0]).switchToCategoriesView();
    }
}