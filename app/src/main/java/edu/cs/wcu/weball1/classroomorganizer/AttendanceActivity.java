package edu.cs.wcu.weball1.classroomorganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

/**
 * ############################################################
 * ##                                                        ##
 * ##   WARNING: This class is NOT currently being used!!!   ##
 * ##                                                        ##
 * ############################################################
 *
 * Displays the lists of students currently enrolled in the course. Allows the instructor to take
 * attendance for the class and click the buttons at the bottom of the screen to see the list of
 * students that are absent, present, and tardy.
 *
 * @author Evert Ball
 * @version 25 March 2020
 */
public class AttendanceActivity extends AppCompatActivity {


    /** The RecyclerView that displays the list of students */
    private RecyclerView recView;

    /** The adapter required by the RecyclerView to display the students */
    private AttendanceAdapter adapter;

    /** The LayoutManager required to display the RecyclerView in the desired format. */
    private RecyclerView.LayoutManager manager;

    /** The list of students to display in the RecyclerView */
    private List<Student> students;

    /** The course that is currently selected to take attendance for. */
    private Course course;

    /** The button to show the present students */
    private Button presentBtn;

    /** The button to show the absent students */
    private Button absentBtn;

    /** The button to show the tardy students */
    private Button tardyBtn;

    /** The current attendance screen we are on (absent, present, or tardy). */
    private String attendanceType;

    FragmentManager fragMan;

    Fragment absentFrag;

    /**
     * Runs at startup and is a part of the Android Activity Lifecycle.
     * @param savedInstanceState The previously saved state of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Required code
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // Initialize to absent screen
        attendanceType = "absent";

        // Getting references to the buttons
        this.presentBtn = findViewById(R.id.btn_present);
        this.absentBtn  = findViewById(R.id.btn_absent);
        this.tardyBtn   = findViewById(R.id.btn_tardy);

        // Creating the list of students from an XML string-array
        String[] stdList = this.getResources().getStringArray(R.array.cs101);
        this.course = new Course();
        for(int i = 0; i < stdList.length; i++) {
            String[] nameArr = stdList[i].split(" ");
            course.addStudent(nameArr[0], nameArr[1], "92000000" + (i+1));
        }

        // Setting up the components for the RecyclerView to display the list correctly
        students = course.getList(attendanceType);
        recView = findViewById(R.id.rv_attendance);
        adapter = new AttendanceAdapter(students/*, this*/);
        manager = new LinearLayoutManager(this);

        // Set up the actions that happen when items are swiped on the RecyclerView
        SwipeControllerActions actions = new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                Student student = students.get(position);
                switch(attendanceType.toLowerCase().trim()) {
                    case "absent":
                        course.mark(student, "present");
                        Toast.makeText(AttendanceActivity.this,
                                student.getFullName() + " Marked Present...",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case "present":
                        course.mark(student, "tardy");
                        Toast.makeText(AttendanceActivity.this,
                                student.getFullName() + " Marked Tardy...",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case "tardy":
                        course.mark(student, "absent");
                        Toast.makeText(AttendanceActivity.this,
                                student.getFullName() + " Marked Absent...",
                                Toast.LENGTH_SHORT).show();
                        break;
                } // end switch
            } // end onRightClicked method

            @Override
            public void onLeftClicked(int position) {
                Student student = students.get(position);
                switch(attendanceType.toLowerCase().trim()) {
                    case "absent":
                        course.mark(student, "tardy");
                        Toast.makeText(AttendanceActivity.this,
                                student.getFullName() + " Marked Tardy...",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case "present":
                        course.mark(student, "absent");
                        Toast.makeText(AttendanceActivity.this,
                                student.getFullName() + " Marked Absent...",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case "tardy":
                        course.mark(student, "present");
                        Toast.makeText(AttendanceActivity.this,
                                student.getFullName() + " Marked Present...",
                                Toast.LENGTH_SHORT).show();
                        break;
                } // end switch
            } // end onLeftClicked method
        }; // end concrete implementation of abstract class

        // Attaching our swipe controller to the RecyclerView
        final SwipeController swipeController = new SwipeController(actions);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recView);

        recView.setHasFixedSize(true); // performance boost
        recView.setAdapter(adapter);
        recView.setLayoutManager(manager);
        recView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    } // end onCreate method

    /**
     * When one of the buttons at the bottom of this activity are clicked, it determines which
     * list to display to the user.
     *
     * @param v The button that was pressed.
     */
    public void attendanceBtnClicked(View v) {
        if(v == presentBtn) {
            attendanceType = "present";
            Toast.makeText(this, "Showing present students...", Toast.LENGTH_SHORT).show();
            recView.setAdapter(new AttendanceAdapter(course.getList("present")));
        } else if(v == absentBtn) {
            attendanceType = "absent";
            Toast.makeText(this, "Showing absent students...", Toast.LENGTH_SHORT).show();
            recView.setAdapter(new AttendanceAdapter(course.getList("absent")));
        } else if(v == tardyBtn) {
            attendanceType = "tardy";
            Toast.makeText(this, "Showing tardy students...", Toast.LENGTH_SHORT).show();
            recView.setAdapter(new AttendanceAdapter(course.getList("tardy")));
        }
    } // end attendanceBtnClicked method

} // end AttendanceActivity class
