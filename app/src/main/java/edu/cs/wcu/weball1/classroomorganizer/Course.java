package edu.cs.wcu.weball1.classroomorganizer;


import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * A course that contains a list of students, the name of the course, and the 
 * semester the course is being held in.
 *
 * @author Evert Ball
 * @version 1.0.0
 */
public class Course {

    /** List of students enrolled in the course */
    private ArrayList<Student> roster;

    /** The name of the course */
    private String courseName;

    /** The semester this course is being held in */
    private String semester;
    
    /**
     * Constructs a course with an empty roster and no course name or 
     * semester.
     */
    public Course() {

        this.roster = new ArrayList<>();
        this.courseName = null;
        this.semester = null;

    } // end empty constructor

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return this.courseName;
    }

    /**
     * Adds a new student to this course.
     *
     * @param fname The student's first name.
     * @param surname The student's last name.
     * @param id The students identification number AKA 920#.
     */
    public void addStudent(String fname, String surname, String id) {
        Student std = new Student(fname, surname, id);
        roster.add(std);
        mark(std, "absent");
    } // end addStudent method

    public void addStudents(ArrayList<Student> stdList) {
        for(Student std: stdList) {
            if(std.getAttendance().equals("")) {
                mark(std, "absent");
            }
        }
        roster = stdList;
    }
    
    /**
     * Returns the number of students enrolled in this course
     *
     * @return The number of students currently enrolled in this course.
     */
    public int getNumStudents() {

        return this.roster.size();

    } // end getNumStudents method
    
    /** 
     * Returns the roster of students.
     *
     * @return An ArrayList of Students.
     */
    public ArrayList<Student> getRoster() {
        
        return this.roster;

    } // end getRoster method
    
    /**
     * Prints the entire roster for this course.
     */
    public void printRoster() {
        for(int i = 0; i < this.roster.size() - 1; i++) {
            System.out.println(this.roster.get(i).toString());
        }
    } // end printRoster method

    /**
     * Gets the list of students that are present.
     * @return An ArrayList of Students that are present.
     */
    private ArrayList<Student> getPresentList() {
        ArrayList<Student> present = new ArrayList<>();
        for(Student std : roster) {
            if (std.getAttendance().toLowerCase().equals("present")) {
                present.add(std);
            }
        }
        return present;
    }

    /**
     * Gets the list of students that are absent.
     * @return An ArrayList of Students that are absent.
     */
    private ArrayList<Student> getAbsentList() {
        ArrayList<Student> absent = new ArrayList<>();
        for(Student std : roster) {
            if (std.getAttendance().toLowerCase().equals("absent")) {
                absent.add(std);
            }
        }
        return absent;
    }

    /**
     * Gets the list of students that are tardy.
     * @return An ArrayList of Students that are tardy.
     */
    private ArrayList<Student> getTardyList() {
        ArrayList<Student> tardy = new ArrayList<>();
        for(Student std : roster) {
            if (std.getAttendance().toLowerCase().equals("tardy")) {
                tardy.add(std);
            }
        }
        return tardy;
    }

    /**
     * Marks the students attendance.
     * @param std The student to mark attendance for.
     * @param attendance The String value for the student to be marked (present, absent, or tardy).
     */
    public void mark(Student std, String attendance) {
        std.setAttendance(attendance);
    }

    public ArrayList<Student> getList(String list) {

        ArrayList<Student> returnList = null;

        switch(list.toLowerCase()) {
            case "present":
                returnList = getPresentList();
                break;
            case "absent":
                returnList = getAbsentList();
                break;
            case "tardy":
                returnList = getTardyList();
                break;
            default:
                returnList = new ArrayList<Student>();
        } // end switch;

        return returnList;
    }

} // end Course class
