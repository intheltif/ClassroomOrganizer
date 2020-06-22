package edu.cs.wcu.weball1.classroomorganizer.ui.main;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.HashMap;

import edu.cs.wcu.weball1.classroomorganizer.AbsentFragment;
import edu.cs.wcu.weball1.classroomorganizer.PresentFragment;
import edu.cs.wcu.weball1.classroomorganizer.TardyFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 *
 * TODO: Convert to FragmentStateAdapter using ViewPager2 class
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    /** Constants to represent the index of each of the tabs */
    private static final int PRESENT_INDEX = 0;
    private static final int ABSENT_INDEX = 1;
    private static final int TARDY_INDEX = 2;

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
     */
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mPageReferenceMap = new HashMap<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mPageReferenceMap.put(position, fragment);
        return fragment;
    } // end instantiateItem method

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    } // end destroyItem method

    public Fragment getFragment(int key) {
        return mPageReferenceMap.get(key);
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
    public int getCount() {
        // Show 3 total pages.
        return 3;
    } // end getCount method

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
        } // end switch
        return title;
    } // end getPageTitle method

}