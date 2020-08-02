package edu.cs.wcu.weball1.classroomorganizer;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * The Fragment that displays the list of absent students in the TabLayout.
 *
 * @author Evert Ball
 * @version 07/06/2020
 *
 */
public class AbsentFragment extends Fragment {

    /** The RecyclerView's adapter */
    private AttendanceAdapter adapter;
    /** The data model that allows us to pass information between tabs */
    private SharedViewModel model;
    /** The list of students in this tab */
    private List<Student> studentList;
    /** The course we are taking attendance for */
    private Course course;
    /** The Dialog used to add a new student */
    private Dialog dialog;

    /**
     * The empty required constructor.
     */
    public AbsentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AbsentFragment.
     */
    public static AbsentFragment newInstance() {
        return new AbsentFragment();
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     *
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     *
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        studentList = model.getAbsentList();
        course = model.getCourse();
        dialog = new Dialog(getContext());

        // The button on the bottom right of the activity that allows us to add students
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.dialog_add_student);
                dialog.setTitle("Add New Student");
                dialog.show();

                final Button btnAdd = dialog.findViewById(R.id.btn_add);
                final Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                final EditText etFirstName = dialog.findViewById(R.id.et_first_name);
                final EditText etLastName = dialog.findViewById(R.id.et_last_name);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    /**
                     * Called when a view has been clicked.
                     *
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {
                        String first = etFirstName.getText().toString();
                        String last = etLastName.getText().toString();
                        if( !first.equals("") && !last.equals("")) {
                            Student std = new Student(first, last, "92000000" + adapter.getItemCount());
                            adapter.addItem(0, std);
                            model.appendToList(std, "new", "absent");
                            dialog.dismiss();
                        } else {
                            Toast.makeText(dialog.getContext(),
                                    "The values for the student's name are missing.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    /**
                     * Called when a view has been clicked.
                     *
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


            } // end onClick method
        }); // end setOnClickListener
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * <p>If you return a View from here, it will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout rl = (RelativeLayout)inflater.inflate(R.layout.fragment_absent,
                container, false);
        RecyclerView recView = rl.findViewById(R.id.rv_absent);
        setUpRecyclerView(recView);


        // Set up the actions that happen when items are swiped on the RecyclerView
        SwipeControllerActions actions = new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                Student student = studentList.get(position);
                course.mark(student, "present");
                refreshData(position, "present", student);
            } // end onRightClicked method

            @Override
            public void onLeftClicked(int position) {
                Student student = studentList.get(position);
                course.mark(student, "tardy");
                refreshData(position, "tardy", student);
            } // end onLeftClicked method
        }; // end concrete implementation of abstract class


        // Attaching our swipe controller to the RecyclerView
        final SwipeController swipeController = new SwipeController(actions);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recView);

        recView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        return recView;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        super.onResume();
        adapter.updateList(model.getAbsentList());
    }

    /**
     * Creates the needed objects to instantiate the recyclerview that displays the student list.
     *
     * @param recList The RecyclerView object.
     */
    private void setUpRecyclerView(RecyclerView recList) {
        // create layout manager and recyclerview adapter objects
        LinearLayoutManager LinearManager = new LinearLayoutManager(this.getContext());
        adapter = new AttendanceAdapter(this.studentList);

        //set the recyclerview objects manager and adapter to the objects created above
        recList.setLayoutManager(LinearManager);
        recList.setAdapter(adapter);

        //performance enhancement
        recList.setHasFixedSize(true);

    } // end setUpRecyclerView method

    /**
     * Notifies the adapter that the data set has changed and removes the item.
     */
    public void refreshData(int position, String destination, Student student) {

        model.appendToList(student, "absent", destination);
        adapter.removeAt(position);
        adapter.notifyDataSetChanged();
    }

    /**
     * Marks all absent students present by using the adapter to move them and the model to update
     * them behind the scenes.
     */
    public void moveAllToPresent() {
        int indexZero = 0;
        for (Student student : studentList) {
            if(student != null) {
                course.mark(student, "present");
                adapter.removeAt(indexZero);
                adapter.notifyDataSetChanged();
            }
        }
        model.moveAllToPresent("absent");
    }

} // end AbsentFragment class