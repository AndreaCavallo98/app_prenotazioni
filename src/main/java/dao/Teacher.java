package dao;

import java.util.ArrayList;

public class Teacher {

    private int id;
    private String name;
    private String surname;

    private ArrayList<Course> teached_courses;

    private double hourly_rate;

    private int num_lectures_given;

    private int num_reviews;

    private double reviews_average;

    private String image_name;

    private Boolean active;

    // Constructor for set
    public Teacher(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    // Constructor for get
    public Teacher(int id, String name, String surname, ArrayList<Course> teached_courses, double hourly_rate, int num_lectures_given, int num_reviews, double reviews_average, String image_name, Boolean active){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.teached_courses = teached_courses;
        this.hourly_rate = hourly_rate;
        this.num_lectures_given = num_lectures_given;
        this.num_reviews = num_reviews;
        this.reviews_average = reviews_average;
        this.image_name = image_name;
        this.active = active;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public ArrayList<Course> getTeached_courses() {
        return teached_courses;
    }

    public void setTeached_courses(ArrayList<Course> teached_courses) {
        this.teached_courses = teached_courses;
    }

    public double getHourly_rate() {
        return hourly_rate;
    }

    public void setHourly_rate(double hourly_rate) {
        this.hourly_rate = hourly_rate;
    }

    public int getNum_lectures_given() {
        return num_lectures_given;
    }

    public void setNum_lectures_given(int num_lectures_given) {
        this.num_lectures_given = num_lectures_given;
    }

    public int getNum_reviews() {
        return num_reviews;
    }

    public void setNum_reviews(int num_reviews) {
        this.num_reviews = num_reviews;
    }

    public double getReviews_average() {
        return reviews_average;
    }

    public void setReviews_average(double reviews_average) {
        this.reviews_average = reviews_average;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    @Override
    public String toString() {
        return id + "] " + name + " " + surname + " stato: " + active;
    }


}
