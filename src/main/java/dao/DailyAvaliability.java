package dao;

public class DailyAvaliability {

    private String course;
    private String teacher;
    private String color;

    public DailyAvaliability(String course, String teacher, String color){
        this.course = course;
        this.teacher = teacher;
        this.color = color;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
