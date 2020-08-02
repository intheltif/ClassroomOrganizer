package edu.cs.wcu.weball1.classroomorganizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

/**
 * The splash screen of the application. Delays for a few moments before moving on to the attendance
 * activity screen.
 *
 * @author Evert Ball
 * @version 25 March 2020
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /** The Drawer Layout that contains our side navigation drawer. */
    private DrawerLayout drawer;

    /** The toggle that allows us to access the drawer from the toolbar */
    private ActionBarDrawerToggle toggle;

    /**
     * Runs at the moment this activity is created. This will happen at the start of the application
     * being invoked by the user.
     * @param savedInstanceState The previous state of this application. Not currently used.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    } // end onCreate method

    /**
     * Called when activity start-up is complete (after onStart() and onRestoreInstanceState(Bundle)
     * have been called). Part of the required methods for Side Drawer Navigation.
     *
     * @param savedInstanceState The data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    } // end onPostCreate method

    /**
     * Called by the system when the device configuration changes while your activity is running.
     * Note that this will only be called if you have selected configurations you would like to
     * handle with the R.attr.configChanges attribute in your manifest. If any configuration change
     * occurs that is not selected to be reported by that attribute, then instead of reporting it
     * the system will stop and restart the activity (to have it launched with the new
     * configuration). At the time that this function has been called, your Resources object will
     * have been updated to return resource values matching the new configuration.
     *
     * @param newConfig The new device configuration. This value cannot be null.
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    } // end onConfigurationChanged method

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
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    } // end onOptionsItemSelected method

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_attendance:
                Intent attendanceIntent = new Intent(this, TabbedAttendanceActivity.class);
                startActivity(attendanceIntent);
                break;
            case R.id.nav_gradebook:
                Toast.makeText(this, "Gradebook activity has not yet been implemented...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_notes:
                Toast.makeText(this, "Notes activity has not yet been implemented...", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    } // end onNavigationItemSelected method

    /**
     * Performs all of the necessary work for implementing the navigation drawer and toolbar.
     */
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        setSupportActionBar(toolbar);
        drawer.addDrawerListener(toggle);
        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
    } // end setUpToolbar method

} // end MainActivity class
