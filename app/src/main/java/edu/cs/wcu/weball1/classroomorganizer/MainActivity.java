package edu.cs.wcu.weball1.classroomorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * The splash screen of the application. Delays for a few moments before moving on to the attendance
 * activity screen.
 *
 * @author Evert Ball
 * @version 25 March 2020
 */
public class MainActivity extends AppCompatActivity {

    /** The amount of time to stay on the splash screen */
    private static final int DELAY = 2000;

    /** The Runnable object that allows delaying the main thread */
    private Runnable runner = new Runnable() {
        @Override
        public void run() {
            goToAttendanceActivity();
        }
    };

    /**
     * Runs at the moment this activity is created. This will happen at the start of the application
     * being invoked by the user.
     * @param savedInstanceState The previous state of this application. Not currently used.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called at the start of this activity. Allows us to delay the application by a set amount of
     * time.
     */
    @Override
    protected void onStart() {
        super.onStart();

        Handler handler = new Handler();
        handler.postDelayed(runner, DELAY);
    }

    /**
     * Starts the attendance activity, which is the next activity after
     * the splash screen is displayed.
     */
    private void goToAttendanceActivity() {
        Intent attendance = new Intent(this, TabbedAttendanceActivity.class);
        startActivity(attendance);
    }
}
