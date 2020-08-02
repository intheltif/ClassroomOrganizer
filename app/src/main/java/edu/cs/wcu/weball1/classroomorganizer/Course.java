package edu.cs.wcu.weball1.classroomorganizer;


import java.util.ArrayList;

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

    /**
     * Constructs a course with an empty roster and no course name or 
     * semester.
     */
    public Course() {

        this.roster = new ArrayList<>();
        this.courseName = null;

    } // end empty constructor

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void addStudents(ArrayList<Student> stdList) {
        for(Student std: stdList) {
            if(std.getAttendance().equals("")) {
                mark(std, "absent");
            }
        }
        roster = stdList;
    }

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
