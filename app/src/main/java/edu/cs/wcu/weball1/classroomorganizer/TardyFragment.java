package edu.cs.wcu.weball1.classroomorganizer;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TardyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TardyFragment extends Fragment {

    private AttendanceAdapter adapter;
    private SharedViewModel model;
    private List<Student> studentList;
    private Course course;

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
        TardyFragment fragment = new TardyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        studentList = model.getTardyList();
        course = model.getCourse();
    }

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
                refreshData(position, "absent", student);
            } // end onRightClicked method

            @Override
            public void onLeftClicked(int position) {
                Student student = studentList.get(position);
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
}