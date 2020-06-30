package edu.cs.wcu.weball1.classroomorganizer;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import edu.cs.wcu.weball1.classroomorganizer.ui.main.SectionsPagerAdapter;

public class TabbedAttendanceActivity extends AppCompatActivity {

    private static final int PRESENT_TAB_INDEX = 0;
    private static final int ABSENT_TAB_INDEX = 1;
    private static final int TARDY_TAB_INDEX = 2;

    SharedViewModel model;

    /**
     * Called when the activity is starting. This is where most initialization goes.
     *
     * @param savedInstanceState If the activity is being reinitialized after previously being shut
     *                           down then this Bundle contains the data it most recently supplied
     *                           in onSaveInstanceState(Bundle), otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_attendance);


        // Set up viewpager, adapter, and tab layout
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this,
                getSupportFragmentManager(), getLifecycle());
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);

        // Get page titles through mediator
        new TabLayoutMediator(tabs, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case PRESENT_TAB_INDEX:
                                tab.setText("Present");
                                break;
                            case ABSENT_TAB_INDEX:
                                tab.setText("Absent");
                                break;
                            case TARDY_TAB_INDEX:
                                tab.setText("Tardy");
                                break;
                            default:
                                tab.setText("Unknown");
                        } // end switch
                    } // end onConfigureTab method
                } // end anonymous class
        ).attach();

        // Disable paging between tabs in ViewPager2 to allow RecyclerView to handle touch events
        viewPager.setUserInputEnabled(false);

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
        Course course = new Course();
        for(int i = 0; i < stdList.length; i++) {
            String[] nameArr = stdList[i].split(" ");
            course.addStudent(nameArr[0], nameArr[1], "92000000" + (i+1));
        }

        // Set up model to load shared persistent data
        model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.setAbsentList(course.getList("absent"));
        model.setPresentList(course.getList("present"));
        model.setTardyList(course.getList("tardy"));
        model.setCourse(course);


    } // end onCreate method

} // end class