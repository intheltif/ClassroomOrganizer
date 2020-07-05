package edu.cs.wcu.weball1.classroomorganizer;

import androidx.lifecycle.ViewModel;

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

} // end SharedViewModel class
