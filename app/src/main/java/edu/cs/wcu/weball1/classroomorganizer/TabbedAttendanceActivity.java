package edu.cs.wcu.weball1.classroomorganizer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import edu.cs.wcu.weball1.classroomorganizer.ui.main.SectionsPagerAdapter;

public class TabbedAttendanceActivity extends AppCompatActivity {

    private static final int PRESENT_TAB_INDEX = 0;
    private static final int ABSENT_TAB_INDEX = 1;
    private static final int TARDY_TAB_INDEX = 2;

    private Course course;
    SharedViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_attendance);


        // Set up viewpager, adapter, and tab layout
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        // TODO: Convert to ViewPager2
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

        // Creating the list of students from an XML string-array
        String[] stdList = this.getResources().getStringArray(R.array.cs101);
        this.course = new Course();
        for(int i = 0; i < stdList.length; i++) {
            String[] nameArr = stdList[i].split(" ");
            course.addStudent(nameArr[0], nameArr[1], "92000000" + (i+1));
        }

        // Set up model to load shared persistent data
        model = ViewModelProviders.of(this).get(SharedViewModel.class);
        model.setAbsentList(course.getList("absent"));
        model.setPresentList(course.getList("present"));
        model.setTardyList(course.getList("tardy"));
        model.setCourse(course);


    } // end onCreate method

} // end class