package edu.cs.wcu.weball1.classroomorganizer;

import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * A course that contains a list of students, the name of the course, and the 
 * semseter the course is being held in.
 *
 * @author Evert Ball
 * @version 1.0.0
 */
public class Course {

    /** List of students enrolled in the course */
    private ArrayList<Student> roster;
    private Scanner scan;

    /** The name of the course */
    private String courseName;

    /** The semeseter this course is being held in */
    private String semester;
    
    /**
     * Constructs a course with an empty roster and no course name or 
     * semeseter.
     */
    public Course() {

        this.roster = new ArrayList<>();
        this.courseName = null;
        this.semester = null;
        this.scan = null;

    } // end empty constructor

    /**
     * Constructs a course with a populated roster but no semester or course
     * name.
     *
     * @param filename The name of the file to populate the roster from.
     */
    public Course(String filename) {

        this.roster = new ArrayList<>();
        this.courseName = null;
        this.semester = null;
        try {
            this.scan = new Scanner(new File(filename));
        } catch(FileNotFoundException fnfe) {
            System.err.println("File not found...");
            System.exit(1);
        }
        
        populateCourse();

    } // end only filename constructor
    
    /**
     * Constructs a populated course with a course name and semester.
     *
     * @param filename The name of the file to populate the roster from.
     * @param name     The name of the course (i.e. CS 495).
     * @param semester The semester this course is being held during.
     */
    public Course(String filename, String name, String semester) throws FileNotFoundException {

        this.roster = new ArrayList<>();
        this.courseName = name;
        this.semester = semester;

        this.scan = new Scanner(new File(filename));
        try {
            this.scan = new Scanner(new File(filename));
        } catch(FileNotFoundException fnfe) {
            System.err.println("File not found...");
            System.exit(1);
        }

        populateCourse();

    } // end full constructor

    /**
     * Adds students that have been read in from a file to the course.
     */
    private void populateCourse() {

        int count = 0;
        while(scan.hasNextLine()) {
            String fullname = scan.nextLine().trim();
            String[] nameArr = fullname.split(" ");
            addStudent(nameArr[0], nameArr[1], "92000000" + count);
            count++;
        }

        this.scan.close();

    } // end populateCourse method
    
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
