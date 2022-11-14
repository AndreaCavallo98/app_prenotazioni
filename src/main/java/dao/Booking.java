package dao;

public class Booking {

    private int id;
    private int idCourse;
    private int idTeacher;
    private int idUser;
    private String date;
    private int startTime;
    private int endTime;

    // Constructor for get
    public Booking(int id, int idCourse, int idTeacher, int idUser, String date, int startTime, int endTime){
        this.id = id;
        this.idCourse = idCourse;
        this.idTeacher = idTeacher;
        this.idUser = idUser;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Constructor for set
    public Booking(int idCourse, int idTeacher, int idUser){
        this.idCourse = idCourse;
        this.idTeacher = idTeacher;
        this.idUser = idUser;
    }

    // Getter & Setter
    public int getId() {
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
    }
}
