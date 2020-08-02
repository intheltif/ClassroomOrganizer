package edu.cs.wcu.weball1.classroomorganizer;

/**
 * A student that can enroll in courses.
 *
 * @author Evert Ball
 * @version 25 March 2020
 */
public class Student {

    /** The photo of this student */
    private String photoPathname;

    /** The attendance for this student. */
    private String attendance;
    
    /** The first name of a student. */
    private String firstName;

    /** The last name of a student. */
    private String surname;

    /** The student's ID aka 920# */
    private String studentID;

    /** The student's email address */
    private String email;
    
    /**
     * Represents a student with a full name as well as a student id, photo, and initial attendance.
     *
     * @param first The first name of the student.
     * @param last The last name of the student.
     * @param id The student's identification number AKA 920#.
     * @param path The pathname to the photo of the student.
     * @param attendance The initial attendance for this student.
     */
    public Student(String id, String first, String last, String email, String path, String attendance) {

        this.studentID = id;
        this.firstName = first;
        this.surname = last;
        this.email = email;
        this.attendance = attendance;
        this.photoPathname = path;

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
        this.email = "";
        this.attendance = "absent";
        this.photoPathname = "";

    } // end constructor without photo

    /**
     * Represents a student with a first and last name as well as a student id.
     *
     * @param first The first name of the student.
     * @param last The last name of the student.
     * @param id The student's identification number AKA 920#.
     */
    public Student(String first, String last, String email, String id) {

        this.firstName = first;
        this.surname = last;
        this.studentID = id;
        this.email = email;
        this.attendance = "absent";
        this.photoPathname = "";

    } // end constructor

    /**
     * Empty constructor that constructs all values except attendance to empty strings.
     * Attendance is defaulted to absent.
     */
    public Student() {
        this.firstName = "";
        this.surname = "";
        this.studentID = "";
        this.email = "";
        this.attendance = "absent";
        this.photoPathname = "";
    }

    /**
     * Returns the student's first name.
     *
     * @return The first name of the student as a String.
     */
    public String getFirstName() {

        return this.firstName;

    } // end getFirstName method

    /**
     * Sets the student's first name.
     *
     * @param fname The first name of the student as a String.
     */
    public void setFirstName(String fname) {

        this.firstName = fname;

    } // end setFirstName method


    /**
     * Returns the student's last name.
     *
     * @return The last name of the student as a String.
     */
    public String getSurname() {

        return this.surname;

    } // end getSurname method

    /**
     * Sets the student's last name.
     *
     * @param surname The last name of the student as a String.
     */
    public void setSurname(String surname) {

        this.surname = surname;

    } // end setSurname method

    /**
     * Returns the full name of this student separated by a single space.
     *
     * @return A string representing the student's full name.
     */
    public String getFullName() {

        return this.firstName + " " + this.surname;

    } // end getFullName method

    /**
     * Sets the first and last name of this student, trimming any trailing whitespace.
     *
     * @param first The first name of the student.
     * @param last The surname of the student.
     */
    public void setFullName(String first, String last) {
        this.firstName = first.trim();
        this.surname = last.trim();
    }

    /** 
     * Returns the ID of the student.
     *
     * @return A string representing the ID for this student.
     */
    public String getID() {

        return this.studentID;

    } // end getID method

    /**
     * Sets the id of the student as a string value.
     *
     * @param stdID the 920# of the student AKA the student's id
     */
    public void setID(String stdID) {
        this.studentID = stdID;
    }

    /**
     * Returns a Drawable resource containing the Student's photo.
     *
     * @return A Drawable resource that is the photo of this student.
     */
    public String getPhotoPathname() {

        return this.photoPathname;

    } // end getPhoto method

    /**
     * Sets the image of the student.
     * @param path The pathname to the student's photo.
     */
    public void setPhotoPath(String path) {
        this.photoPathname = path;
    }

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
        this.attendance = attendance;
    }

    /**
     * Sets the email for this student.
     * @param email This students current email address as a String.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the email for this student.
     */
    public String getEmail() {
        return this.email;
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

    /**
     * Returns this student's data in the correct format for writing to the CSV file.
     * @return The correctly formated String to write to the CSV file.
     */
    public String toCSV() {
        return this.studentID +
                "," +
                this.firstName +
                "," +
                this.surname +
                "," +
                this.email +
                "," +
                this.photoPathname +
                "," +
                this.attendance +
                "\n";
    } // end toCSV method

} // end Student class
