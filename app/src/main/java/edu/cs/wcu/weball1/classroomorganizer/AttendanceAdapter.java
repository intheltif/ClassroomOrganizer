package edu.cs.wcu.weball1.classroomorganizer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * An adapter class that provides the functionality for the AttendanceActivity RecyclerView
 * component.
 *
 * @author Evert Ball
 * @version 25 March 2020
 */
public class AttendanceAdapter
        extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    /** The roster of students to display on the screen. */
    public List<Student> roster;

    /**
     * The constructor for this adapter.
     * @param roster The student roster to display.
     */
    public AttendanceAdapter(List<Student> roster) {
        this.roster = roster;
    } // end constructor

    /**
     * Gets the number of students that will be displayed, used by the RecyclerView
     *
     * @return the number of components that the RecyclerView needs to display.
     */
    public int getItemCount() {
        return roster.size();
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder that is updated to represent the contents of the item
     *               at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    public void onBindViewHolder(AttendanceViewHolder holder, int position) {

        Student student = roster.get(position);
        //TODO: Implement photos for each student.
        //holder.studentPhoto.setImageDrawable(student.getPhoto());
        holder.firstName.setText(student.getFirstName());
        holder.lastName.setText(student.getSurname());
        holder.root.setTag(student);

    } // end onBindViewHolder method

    /**
     * Called when RecyclerView needs a new ViewHolder to represent an item. This new ViewHolder
     * represents the list of students. It is inflated from an existing XML layout file.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an
     *               adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder of type AttendanceViewHolder.
     */
    public AttendanceAdapter.AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
            .inflate(R.layout.student_card, parent, false);

        ImageView img = l.findViewById(R.id.iv_student_photo);
        TextView first = l.findViewById(R.id.tv_first_name);
        TextView last = l.findViewById(R.id.tv_last_name);

        AttendanceViewHolder avh = new AttendanceViewHolder(l, img, first, last);
        return avh;
    } // end onCreateViewHolder method

    public void removeAt(int position) {
        //roster.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, roster.size());
    }

    public void updateList(List<Student> list) {
        roster = list;
        notifyDataSetChanged();
    }


    /**
     * The ViewHolder that holds the view for our layout. Required by the RecyclerView.
     */
    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        /** The root view. */
        public View root;
        /** The ImageView that holds the student photo. */
        public ImageView studentPhoto;
        /** The TextView that holds the student's first name. */
        public TextView firstName;
        /** The TextView that holds the student's surname. */
        public TextView lastName;

        /**
         * Constructor for this ViewHolder.
         * @param root The root View.
         * @param studentPhoto The ImageView that holds the photo of the student.
         * @param firstName The TextView that holds the first name of the student
         * @param lastName The TextView that holds the last name of the student.
         */
        public AttendanceViewHolder(View root, ImageView studentPhoto, TextView firstName, TextView lastName) {
            super(root);
            this.root = root;
            this.studentPhoto = studentPhoto;
            this.firstName = firstName;
            this.lastName = lastName;
        } // end constructor
    } // end AttendanceViewHolder inner class

} // end AttendanceAdapter class
