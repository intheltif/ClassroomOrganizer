package edu.cs.wcu.weball1.classroomorganizer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.cs.wcu.weball1.classroomorganizer.ui.main.SectionsPagerAdapter;

/**
 * The activity that allows a user to take attendance.
 * This activity sets up the ViewPager and connects it to the TabLayout to display each of the
 * three attendance types as Fragments that will interact with each other to share student
 * attendance data.
 *
 * TODO: Add onActivityResult method to handle image selection of students.
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

    /** Request code for loading a CSV */
    private static final int LOAD_REQ_CODE = 123;

    /** The data model that allows us to pass data between the different tabs */
    SharedViewModel model;

    /** The ViewPager that assists us in setting up the tab layout */
    ViewPager2 viewPager;

    /** The TabLayout that displays each fragment */
    TabLayout tabs;

    /** The ViewPager's adapter that is required by the ViewPager */
    SectionsPagerAdapter sectionsPagerAdapter;

    /** The current day in the format dd_mm_yyyy. Used to append to filenames */
    private String currDate;

    /** The File to send by email/text/etc that contains the students attendance. */
    private File fileToSend;

    /** Current Course instance */
    private Course course;


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

        // Get today's date as a string
        SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy", Locale.US);
        Date date = new Date();
        currDate = formatter.format(date);

        //Set up the toolbar and allow back button
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if(bar != null) {
            //show usable back button
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
            //show title
            bar.setDisplayShowTitleEnabled(false);

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
        setUpCourse("CS101");
        // Set up model to load shared persistent data
        updateSharedPersistentDataForCourse(course);
        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setDisplayShowTitleEnabled(true);
            bar.setTitle("Course: " + course.getCourseName().toUpperCase());
        }
    } // end onCreate method

    /**
     * Create the course from the saved CSV file, or from the raw resource file if the
     * required CSV doesn't exist
     */
    private void setUpCourse(String courseName) {

        course = new Course();
        course.setCourseName(courseName);
        File filename = new File(getExternalFilesDir(null),"attendance_" +
                course.getCourseName() + "_" + currDate + ".csv");
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
    } // end updateSharedPersistentDataForCourse method

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
    } // end onCreateOptionsMenu method

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
                onLoadCSVButtonClicked();
                break;
            case R.id.mark_all_btn:
                onMarkAllPresentButtonClicked();
                break;
            case android.R.id.home:
                // Exit without saving
                finish();
        } // end switch

        return super.onOptionsItemSelected(item);
    } // end onOptionsItemSelected method

    /**
     * Saves the attendance record to a CSV file when the save icon is clicked.
     */
    private void onSaveButtonClicked() {

        fileToSend = model.writeToCSV(getApplicationContext(), currDate, course.getCourseName());
        // Allows us to use file:// instead of content:// for sharing the CSV
        if(Build.VERSION.SDK_INT >= 24) {
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        AlertDialog.Builder shareFileAD = new AlertDialog.Builder(this);
        shareFileAD.setTitle("Share CSV?");
        shareFileAD.setMessage("Would you like to share the CSV file you just created?");
        shareFileAD.setPositiveButton(R.string.share_csv, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                if(fileToSend.exists()) {
                    intentShareFile.setType("text/plain");
                    intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileToSend));
                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Attendance for " + currDate);
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Attached is CSV file for " +
                            "the attendance taken on: " + currDate);
                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }
                dialog.dismiss();
            } // end onClick
        }); // end setPositiveButton

        shareFileAD.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            } // end onClick
        }); // end setNegativeButton

        // Show the created dialog
        shareFileAD.create().show();

    } // end onSavedButtonClicked method

    /**
     * Called when the user selects this option from the options menu in the Toolbar.
     *
     * Gets instances of the absent and tardy fragments and forces them to move all their students
     * to the present tab. This means that all student's will now be marked present for the day.
     */
    private void onMarkAllPresentButtonClicked() {
        AbsentFragment absentFragment = (AbsentFragment) getSupportFragmentManager().findFragmentByTag("f1");
        TardyFragment tardyFragment = (TardyFragment) getSupportFragmentManager().findFragmentByTag("f2");
        if(absentFragment != null) {
            absentFragment.moveAllToPresent();
        }
        if(tardyFragment != null) {
            tardyFragment.moveAllToPresent();
        }

    } // end onMarkAllPresentButtonClicked method

    private void onLoadCSVButtonClicked() {
        Intent loadCSVIntent = new Intent(this, LoadActivity.class);
        startActivityForResult(loadCSVIntent, LOAD_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LOAD_REQ_CODE) {
            Bundle extras = data.getExtras();
            String path = "";
            if(extras != null) {
                path = extras.getString("loadedFile");
                course = new Course();
                File file = new File(path);
                //Toast.makeText(this, "Loading from: " + file, Toast.LENGTH_SHORT).show();
                try {
                    InputStream existsStream = new FileInputStream(file);
                    course.addStudents(model.readFromCSV(existsStream));
                    updateSharedPersistentDataForCourse(course);
                    Toast.makeText(this, "Loaded from: " + file, Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException fnfe) {
                    Log.e("TABBED_ACT_RESULT", "Could not find the specified file...");
                    fnfe.printStackTrace();
                }
            }
        }
    }

} // end TabbedAttendanceActivity class