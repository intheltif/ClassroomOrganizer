package edu.cs.wcu.weball1.classroomorganizer.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.HashMap;

import edu.cs.wcu.weball1.classroomorganizer.AbsentFragment;
import edu.cs.wcu.weball1.classroomorganizer.PresentFragment;
import edu.cs.wcu.weball1.classroomorganizer.TardyFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 *
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {

    /** Constants to represent the index of each of the tabs */
    private static final int PRESENT_INDEX = 0;
    private static final int ABSENT_INDEX = 1;
    private static final int TARDY_INDEX = 2;
    private static final int NUM_PAGES = 3;

    /** The application context */
    private final Context mContext;

    /** A mapping of tab positions to fragments. */
    protected HashMap<Integer, Fragment> mPageReferenceMap;

    /**
     * Constructor to create a new SectionsPagerAdapter object with the application context and
     * a given FragmentManager.
     *
     * @param context The application context obtained from the host activity.
     * @param fm The FragmentManager obtained from the host activity.
     * @param lifecycle The Lifcycle object obtained from the host activity.
     */
    public SectionsPagerAdapter(Context context, FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
        mContext = context;
        mPageReferenceMap = new HashMap<>();
    }

    /**
     * Returns the appropriate fragment for the tab position selected.
     *
     * @return A fragment that corresponds to the selected tab position.
     */
    @Override @NonNull
    public Fragment createFragment(int position) {
        Fragment fragment = AbsentFragment.newInstance();
        switch (position) {
            case PRESENT_INDEX:
                fragment = PresentFragment.newInstance();
                break;
            case ABSENT_INDEX:
                // Absent fragment is the default
                break;
            case TARDY_INDEX:
                fragment = TardyFragment.newInstance();
                break;
        } // end switch
        return fragment;
    } // end getItem method

    /**
     * Gets the number of tabs to be shown in the TabLayout.
     *
     * @return The number of tabs to be shown.
     */
    @Override
    public int getItemCount() {
        // Show 3 total pages.
        return NUM_PAGES;
    } // end getCount method

    public Fragment getFragment(int index) {
        return mPageReferenceMap.get(index);
    }

}