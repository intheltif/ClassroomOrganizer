package edu.cs.wcu.weball1.classroomorganizer;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import edu.cs.wcu.weball1.classroomorganizer.ui.main.SectionsPagerAdapter;

/**
 * The activity that allows a user to take attendance.
 * This activity sets up the ViewPager and connects it to the TabLayout to display each of the
 * three attendance types as Fragments that will interact with each other to share student
 * attendance data.
 *
 * @author Evert Ball
 * @version 07/06/2020
 *
 */
public class TabbedAttendanceActivity extends AppCompatActivity {

    /** Constants that represent the index of each of the three attendance tabs */
    private static final int PRESENT_TAB_INDEX = 0;
    private static final int ABSENT_TAB_INDEX = 1;
    private static final int TARDY_TAB_INDEX = 2;

    /** The request code used by startActivityForResult for the gallery image */
    private static final int PICK_IMAGE = 100;

    /** The data model that allows us to pass data between the different tabs */
    SharedViewModel model;

    /** The ViewPager that assists us in setting up the tab layout */
    ViewPager2 viewPager;

    /** The TabLayout that displays each fragment */
    TabLayout tabs;

    /** The ViewPager's adapter that is required by the ViewPager */
    SectionsPagerAdapter sectionsPagerAdapter;

    /** The ImageView for the students photo */
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