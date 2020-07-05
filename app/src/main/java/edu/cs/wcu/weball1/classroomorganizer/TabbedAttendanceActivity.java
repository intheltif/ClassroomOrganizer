package edu.cs.wcu.weball1.classroomorganizer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import edu.cs.wcu.weball1.classroomorganizer.ui.main.SectionsPagerAdapter;

public class TabbedAttendanceActivity extends AppCompatActivity {

    private static final int PRESENT_TAB_INDEX = 0;
    private static final int ABSENT_TAB_INDEX = 1;
    private static final int TARDY_TAB_INDEX = 2;

    /** The request code used by startActivityForResult for the gallery image */
    private static final int PICK_IMAGE = 100;

    SharedViewModel model;
    ViewPager2 viewPager;
    TabLayout tabs;
    SectionsPagerAdapter sectionsPagerAdapter;
    ImageView img;

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

        img = findViewById(R.id.iv_student_photo);

        // Get references to ViewPager2 and TabLayout elements
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);

        // Create ViewPager2's adapter and connect it to the ViewPager2
        sectionsPagerAdapter = new SectionsPagerAdapter(this,
                getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(sectionsPagerAdapter);

        // Request page titles through mediator
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
        // Create course with a list of students.
        Course course = setUpCourse();
        // Set up model to load shared persistent data
        updateSharedPersistentDataForCourse(course);

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

    } // end onCreate method

    /**
     * Create the course from the XML string-array resource in Strings.xml
     *
     * @return The course that was created from the string-array of student names.
     */
    private Course setUpCourse() {

        // Creating the list of students from an XML string-array
        String[] stdList = this.getResources().getStringArray(R.array.cs101);
        Course course = new Course();
        for(int i = 0; i < stdList.length; i++) {
            String[] nameArr = stdList[i].split(" ");
            course.addStudent(nameArr[0], nameArr[1], "92000000" + (i+1));
        }
        return course;
    } // end setUpCourse method

    /**
     * Creates a new ViewModelProvider that shares persistent data between fragments.
     * @param course The course we are currently in.
     */
    private void updateSharedPersistentDataForCourse(Course course) {
        // Load shared persistent data
        model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.setAbsentList(course.getList("absent"));
        model.setPresentList(course.getList("present"));
        model.setTardyList(course.getList("tardy"));
        model.setCourse(course);
    }

    //TODO Find fix for accepting incoming photo from media gallery
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment currFrag = sectionsPagerAdapter.getFragment(viewPager.getCurrentItem());
        currFrag.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Entered onActivityResult", Toast.LENGTH_SHORT).show();
        img = this.findViewById(R.id.iv_student_photo);
        if(resultCode != RESULT_CANCELED && data != null) {
            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "The requested file could not be found.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    Toast.makeText(this, "An I/O error occurred...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (NullPointerException npe) {
                    Toast.makeText(this, "Content resolver returned null...", Toast.LENGTH_SHORT).show();
                    npe.printStackTrace();
                }
                if(img != null) {
                    img.setImageBitmap(bitmap);
                } else {
                    Log.e("UGGGGHHHH", "THE IMAGEVIEW IS NULL, DUMMY!!!!");

                }
            }
        } else {
            Toast.makeText(this, "result cancelled or null val", Toast.LENGTH_SHORT).show();
        }
    }
     */

} // end TabbedAttendanceActivity class