package edu.cs.wcu.weball1.classroomorganizer;

import android.graphics.drawable.Drawable;
import android.net.sip.SipSession;
import android.widget.Toast;

/**
 * A student that can enroll in courses.
 *
 * @author Evert Ball
 * @version 25 March 2020
 */
public class Student {

    /** The photo of this student */
    private Drawable photo;

    /** The attendance for this student. */
    private String attendance;
    
    /** The first name of a student. */
    private String firstName;

    /** The last name of a student. */
    private String surname;

    /** The student's ID aka 920# */
    private String studentID;
    
    /**
     * Represents a student with a full name as well as a student id, photo, and initial attendance.
     *
     * @param first The first name of the student.
     * @param last The last name of the student.
     * @param id The student's identification number AKA 920#.
     * @param photo The photo of the student.
     * @param attendance The initial attendance for this student.
     */
    public Student(String first, String last, String id, Drawable photo, String attendance) {
        
        this.firstName = first;
        this.surname = last;
        this.studentID = id;
        this.attendance = attendance;
        this.photo = photo;

    } // end constructor

    /**
     * Represents a student with a first and last name as well as a student id.
     *
     * @param first The first name of the student.
     * @param last The last name of the student.
     * @param id The student's identification number AKA 920#.
     */
    public Student(String first, String last, String id) {

        this.firstName = first;
        this.surname = last;
        this.studentID = id;
        this.attendance = "absent";

    } // end constructor without photo

    /**
     * Returns the student's first name.
     *
     * @return The first name of the student as a String.
     */
    public String getFirstName() {

        return this.firstName;

    } // end getFirstName method

    /**
     * Returns the student's last name.
     *
     * @return The last name of the student as a String.
     */
    public String getSurname() {

        return this.surname;

    } // end getFirstName method

    /** 
     * Returns the full name of this student seperated by a single space.
     *
     * @return A string representing the student's full name.
     */
    public String getFullName() {

        return this.firstName + " " + this.surname;

    } // end getFullName method
    
    /** 
     * Returns the ID of the student.
     *
     * @return A string representing the ID for this student.
     */
    public String getID() {

        return this.studentID;

    } // end getID method

    /**
     * Returns a Drawable resource containing the Student's photo.
     *
     * @return A Drawable resource that is the photo of this student.
     */
    public Drawable getPhoto() {

        return this.photo;

    } // end getPhoto method

    /**
     * Returns whether the student was absent, present, or tardy.
     * @return The student's current attendance.
     */
    public String getAttendance() {
        return this.attendance;
    }

    /**
     * Sets the attendance for this student.
     * @param attendance This students current attendance as a String.
     */
    public void setAttendance(String attendance) {

        System.out.println("====== Student Marked: " + attendance + " ======");

        this.attendance = attendance;


    }

    /**
     * Converts this object into a string.
     * 
     * @return This object as a string. 
     */
    @Override
    public String toString() {
        return "" + this.studentID + " " + this.firstName + " " + this.surname;
    } // end toString

} // end Student class
