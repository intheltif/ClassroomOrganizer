package edu.cs.wcu.weball1.classroomorganizer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.cs.wcu.weball1.classroomorganizer.ui.main.SectionsPagerAdapter;

public class TabbedAttendanceActivity extends AppCompatActivity {

    private static final int PRESENT_TAB_INDEX = 0;
    private static final int ABSENT_TAB_INDEX = 1;
    private static final int TARDY_TAB_INDEX = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_attendance);

        // Set up viewpager, adapter, and tab layout
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Ensure absent tab is selected at startup
        tabs.selectTab(tabs.getTabAt(ABSENT_TAB_INDEX));

        // TODO: Make fab add a student.
        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */
    }
}