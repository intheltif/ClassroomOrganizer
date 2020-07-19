package edu.cs.wcu.weball1.classroomorganizer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    /** The request code used by startActivityForResult */
    private static final int PICK_IMAGE = 100;

    /** The roster of students to display on the screen. */
    public List<Student> roster;
    /** A reference to the attached RecyclerView. */
    RecyclerView recyclerView;
    /** The current application context */
    Context mContext;
    /** The dialog for editing students. */
    Dialog dialog;

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
    public void onBindViewHolder(final AttendanceViewHolder holder, final int position) {

        final Student student = roster.get(position);
        //TODO: Implement photos for each student.
        //holder.studentPhoto.setImageDrawable(student.getPhoto());
        holder.firstName.setText(student.getFirstName());
        holder.lastName.setText(student.getSurname());
        holder.root.setTag(student);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "on click", Toast.LENGTH_LONG).show();
                dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.dialog_edit_student);
                dialog.setTitle("Edit Student Details");

                final EditText first_editText = dialog.findViewById(R.id.et_edit_first_name);
                first_editText.setText(student.getFirstName());

                final EditText last_editText = dialog.findViewById(R.id.et_edit_last_name);
                last_editText.setText(student.getSurname());

                final ImageView stdImg = dialog.findViewById(R.id.iv_edit_photo);

                stdImg.setOnClickListener(new View.OnClickListener() {
                    /**
                     * Called when a view has been clicked.
                     *
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        ((Activity)mContext).startActivityForResult(intent, PICK_IMAGE);
                    }
                });

                final ImageButton delStd = dialog.findViewById(R.id.btn_delete_student);
                delStd.setOnClickListener(new View.OnClickListener() {
                    /**
                     * Called when a view has been clicked.
                     *
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {
                        removeAt(position);
                        roster.remove(position);
                        recyclerView.getAdapter().notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        student.setFullName(first_editText.getText().toString(), last_editText.getText().toString());
                        recyclerView.getAdapter().notifyItemChanged(position);
                    }
                }); // end cancel listener

            } // end onClick
        }); //end onClick Listener
    } // end onBindViewHolder method

    /**
     * Called by RecyclerView when it starts observing this Adapter.
     * <p>
     * Keep in mind that same adapter may be observed by multiple RecyclerViews.
     *
     * @param recyclerView The RecyclerView instance which started observing this adapter.
     * @see #onDetachedFromRecyclerView(RecyclerView)
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

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

        mContext = parent.getContext();

        ImageView img = l.findViewById(R.id.iv_student_photo);
        TextView first = l.findViewById(R.id.tv_first_name);
        TextView last = l.findViewById(R.id.tv_last_name);

        AttendanceViewHolder avh = new AttendanceViewHolder(l, img, first, last);
        return avh;
    } // end onCreateViewHolder method

    /**
     * Removes the student at the specified position and notifies any observers that the data set
     * has changed.
     *
     * @param position The student's position in the list we are removing them from.
     */
    public void removeAt(int position) {
        //roster.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, roster.size());
    }

    /**
     * Updates the list of students once data has changed.
     *
     * @param list The new list of students.
     */
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
