package dao;

public class RelCourseTeacher {

    private int id;
    private String course;
    private String teacherName;
    private String teacherSurname;

    public RelCourseTeacher(int id, String course, String teacherName, String teacherSurname){
        this.id = id;
        this.course = course;
        this.teacherName = teacherName;
        this.teacherSurname = teacherSurname;
    }

    @Override
    public String toString(){
        return id + "] COURSE NAME: " + course + " --> TEACHER: " + teacherName + " " + teacherSurname;
    }








}
