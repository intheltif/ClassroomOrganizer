package edu.cs.wcu.weball1.classroomorganizer;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * The Fragment that displays the list of tardy students in the TabLayout.
 *
 * @author Evert Ball
 * @version 07/06/2020
 *
 */
public class TardyFragment extends Fragment {

    /** The RecyclerView's adapter */
    private AttendanceAdapter adapter;
    /** The data model that allows us to pass information between tabs */
    private SharedViewModel model;
    /** The list of students in this tab */
    private List<Student> studentList;
    /** The course we are taking attendance for */
    private Course course;

    /**
     * The empty required constructor.
     */
    public TardyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AbsentFragment.
     */
    public static TardyFragment newInstance() {
        return new TardyFragment();
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
        assert getActivity() != null;
        model = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        studentList = model.getTardyList();
        course = model.getCourse();
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
        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.fragment_tardy, container, false);
        RecyclerView recView = rl.findViewById(R.id.rv_tardy);
        setUpRecyclerView(recView);


        // Set up the actions that happen when items are swiped on the RecyclerView
        SwipeControllerActions actions = new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                Student student = studentList.get(position);
                course.mark(student, "absent");
                refreshData(position, "absent", student);
            } // end onRightClicked method

            @Override
            public void onLeftClicked(int position) {
                Student student = studentList.get(position);
                course.mark(student, "present");
                refreshData(position, "present", student);
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
        adapter.updateList(model.getTardyList());
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
        model.appendToList(student, "tardy", destination);
        adapter.removeAt(position);
        adapter.notifyDataSetChanged();
    }

    /**
     * Marks all tardy students present by using the adapter to move them and the model to update
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
        model.moveAllToPresent("tardy");
    }
}