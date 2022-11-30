package dao;

public class Booking {

    private int id;
    private String course_title;
    private String course_color;
    private String teacher_name_surname;

    private int id_user;
    private String date;
    private int start_time;
    private int end_time;
    private boolean confirmed;
    private boolean deleted;

    private boolean has_review;

    // Constructor for get
    public Booking(int id, String course_title, String course_color, String teacher_name_surname, int id_user, String date, int start_time, int end_time, boolean confirmed, boolean deleted, boolean has_review){
        this.id = id;
        this.course_title = course_title;
        this.course_color = course_color;
        this.teacher_name_surname = teacher_name_surname;
        this.id_user = id_user;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.confirmed = confirmed;
        this.deleted = deleted;
        this.has_review = has_review;
    }

    // Constructor for set
    /*public Booking(int idCourse, int idTeacher, int idUser){
        this.idCourse = idCourse;
        this.idTeacher = idTeacher;
        this.idUser = idUser;
    }*/

    // Getter & Setter
    /*public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public int getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString(){
        return id + "] lecture of " + idCourse + " with teacher " + idTeacher + " booked by " + idUser + " | Day: " + date + " from " + startTime + ":00 to " + endTime + ":00";
    }*/
}
