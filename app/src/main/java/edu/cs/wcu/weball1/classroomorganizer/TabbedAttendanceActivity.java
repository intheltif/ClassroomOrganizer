package edu.cs.wcu.weball1.classroomorganizer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    /** The current day in the format dd_mm_yyyy. Used to append to filenames */
    private String currDate;


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

        // Get today's date as a string
        SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy");
        Date date = new Date();
        currDate = formatter.format(date);

        //Set up the toolbar and allow back button
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
        }

        // Set up the ViewModel to allow data to be passed back and forth
        model = new ViewModelProvider(this).get(SharedViewModel.class);
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

        Course course = new Course();
        File filename = new File(getExternalFilesDir(null),"attendance_" + currDate + ".csv");
        if(filename.exists()) {
            try {
                InputStream existsStream = new FileInputStream(filename);
                course.addStudents(model.readFromCSV(existsStream));
            } catch (FileNotFoundException fnfe) {
                Log.e("TABBED", "Could not find the specified file...");
                fnfe.printStackTrace();
            }
        } else {
            InputStream newStream = getResources().openRawResource(R.raw.attendance);
            course.addStudents(model.readFromCSV(newStream));
        }
        return course;
    } // end setUpCourse method

    /**
     * Creates a new ViewModelProvider that shares persistent data between fragments.
     * @param course The course we are currently in.
     */
    private void updateSharedPersistentDataForCourse(Course course) {
        // Load shared persistent data
        model.setAbsentList(course.getList("absent"));
        model.setPresentList(course.getList("present"));
        model.setTardyList(course.getList("tardy"));
        model.setCourse(course);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attendance_actions_menu, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_button:
                onSaveButtonClicked();
                break;
            case R.id.load_csv_button:
                //onLoadCSVButtonClicked();
                break;
            case R.id.mark_all_btn:
                //onMarkAllPresentButtonClicked();
                break;
            case android.R.id.home:
                // Exit without saving
                finish();
        } // end switch

        return super.onOptionsItemSelected(item);
    }

    /**
     * Saves the attendance record to a CSV file when the save icon is clicked.
     */
    private void onSaveButtonClicked() {

        // Allows us to use file:// instead of content:// for sharing the CSV
        if(Build.VERSION.SDK_INT >= 24) {
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File fileToSend = model.writeToCSV(getApplicationContext(), currDate);
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        if(fileToSend.exists()) {
            intentShareFile.setType("application/csv");
            intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileToSend));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Attendance for " + currDate);
            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    } // end onSavedButtonClicked method

    private void onMarkAllPresentButtonClicked() {

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