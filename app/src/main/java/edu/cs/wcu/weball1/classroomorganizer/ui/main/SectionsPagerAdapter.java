package edu.cs.wcu.weball1.classroomorganizer.ui.main;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.cs.wcu.weball1.classroomorganizer.AbsentFragment;
import edu.cs.wcu.weball1.classroomorganizer.PresentFragment;
import edu.cs.wcu.weball1.classroomorganizer.TardyFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Returns the appropriate fragment for the tab position selected.
     *
     * @return A fragment that corresponds to the selected tab position.
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = AbsentFragment.newInstance();
        switch (position) {
            case 0:
                fragment = PresentFragment.newInstance();
                break;
            case 1:
                // Instantiated as absent fragment so no changes needed.
                break;
            case 2:
                fragment = TardyFragment.newInstance();
                break;
        }
        return fragment;
    }

    /**
     * Gets the number of tabs to be shown in the TabLayout.
     *
     * @return The number of tabs to be shown.
     */
    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    /**
     * Returns the title of the tab given its position in the TabLayout.
     *
     * @param position The numerical position in the TabLayout indexed from 0.
     * @return The tab title as a CharSequence.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = "";
        switch (position) {
            case 0:
                title = "Present";
                break;
            case 1:
                title = "Absent";
                break;
            case 2:
                title = "Tardy";
                break;
            default:
                title = "Unknown";
        }
        return title;
    }
}