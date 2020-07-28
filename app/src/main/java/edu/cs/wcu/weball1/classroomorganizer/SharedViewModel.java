package edu.cs.wcu.weball1.classroomorganizer;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A Model class that allows us to pass data between UI objects. A quick and dirty way to implement
 * MVVM architecture. This class essentially acts as a "delivery man" of objects.
 *
 * @author Evert Ball
 * @version 22 June 2020
 */
public class SharedViewModel extends ViewModel {

    /** Constant for logcat output */
    private static final String SVM = "SharedViewModel";

    /** Constants for each of the csv fields */
    private static final int NINE_TWO_INDEX       = 0;
    private static final int FIRST_NAME_INDEX     = 1;
    private static final int LAST_NAME_INDEX      = 2;
    private static final int EMAIL_INDEX          = 3;
    private static final int PHOTO_PATHNAME_INDEX = 4;
    private static final int ATTENDANCE_INDEX     = 5;

    /** Constant to represent empty string length */
    private static final int ZERO = 0;

    /** The students that are currently present. */
    private List<Student> presentList = new ArrayList<>();

    /** The students that are currently absent. */
    private List<Student> absentList = new ArrayList<>();

    /** The students that are currently tardy. */
    private List<Student> tardyList = new ArrayList<>();

    /** The course we are taking attendance for. */
    private Course course;

    /**
     * Holds the list of present students.
     * @param list The list of present students to hold.
     */
    public void setPresentList(List<Student> list) {
        presentList = list;
    }

    /**
     * Holds the list of absent students.
     * @param list The list of absent students to hold.
     */
    public void setAbsentList(List<Student> list) {
        absentList = list;
    }

    /**
     * Holds the list of tardy students.
     * @param list The list of tardy students to hold.
     */
    public void setTardyList(List<Student> list) {
        tardyList = list;
    }

    /**
     * Holds a reference to the current course.
     * @param course The current course.
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Returns a reference to the current course.
     * @return A reference to the current course object.
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Returns the list of present students.
     * @return The list of present students.
     */
    public List<Student> getPresentList() {
        return presentList;
    }

    /**
     * Returns the list of absent students.
     * @return The list of absent students.
     */
    public List<Student> getAbsentList() {
        return absentList;
    }

    /**
     * Returns the list of tardy students.
     * @return The list of tardy students.
     */
    public List<Student> getTardyList() {
        return tardyList;
    }

    /**
     * Moves an item from one list to another.
     *
     * @param student The student that is being moved.
     * @param source The String representation list that the student is being removed from.
     * @param destination The String representation of the list the student is being moved to.
     */
    public void appendToList(Student student, String source, String destination) {
        switch (destination.toLowerCase()) {
            case "present":
                if (source.toLowerCase().equals("absent")) {
                    absentList.remove(student);
                } else if(source.toLowerCase().equals("tardy")) {
                    tardyList.remove(student);
                }
                presentList.add(student);
                break;
            case "absent":
                if (source.toLowerCase().equals("present")) {
                    presentList.remove(student);
                } else if(source.toLowerCase().equals("tardy")) {
                    tardyList.remove(student);
                }
                absentList.add(student);
                break;
            case "tardy":
                if (source.toLowerCase().equals("absent")) {
                    absentList.remove(student);
                } else if(source.toLowerCase().equals("present")) {
                    presentList.remove(student);
                }
                tardyList.add(student);
                break;
        }
    }

    /**
     * Reads in a list of students from a CSV file.
     * The properties are in the following order:
     *     920# | First Name | Last Name | Email | Photo Path | Attendance
     *
     * The CSV may have missing fields, but it most be in the above order.
     *
     * @param resource The csv file in the android resource raw directory (
     */
    protected void readFromCSV(InputStream resource) {
        // Open reader using InputStream parameter
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource, StandardCharsets.UTF_8)
        );

        // Read the data
        String line = "";
        try {
            while((line = reader.readLine()) != null) {
                // split by commas
                String[] tokens = line.split(",");

                Student student = new Student();
                // Create student object from stream and validating input
                switch(tokens.length) {
                    case 6:
                        if(tokens[ATTENDANCE_INDEX].length() > ZERO) {
                            student.setAttendance(tokens[ATTENDANCE_INDEX]);
                        }
                    case 5:
                        if(tokens[PHOTO_PATHNAME_INDEX].length() > ZERO) {
                            student.setPhotoPath(tokens[PHOTO_PATHNAME_INDEX]);
                        }
                    case 4:
                        if(tokens[EMAIL_INDEX].length() > ZERO) {
                            student.setEmail(tokens[EMAIL_INDEX]);
                        }
                    case 3:
                        if(tokens[LAST_NAME_INDEX].length() > ZERO) {
                            student.setSurname(tokens[LAST_NAME_INDEX]);
                        }
                    case 2:
                        if(tokens[FIRST_NAME_INDEX].length() > ZERO) {
                            student.setFirstName(tokens[FIRST_NAME_INDEX]);
                        }
                    case 1:
                        if(tokens[NINE_TWO_INDEX].length() > ZERO) {
                            student.setID(tokens[NINE_TWO_INDEX]);
                        }
                }
            } // end while

            // Close reader and stream
            reader.close();
            resource.close();
        } catch(IOException ioe) {
            // Failed to read the data, that's bad. Use wtf log.
            Log.wtf(SVM, "Error reading student data file on line " + line, ioe);
            ioe.printStackTrace();
        } // end try-catch
    } // end readFromCSV method

} // end SharedViewModel class
