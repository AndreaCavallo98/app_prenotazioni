package dao;

public class Course {

    private int id;
    private String title;

    private String color;
    private String image_name;
    private Boolean active;

    // Constructor for set
    public Course(String title, String color){
        this.title = title;
        this.color = color;
    }

    // Constructor for get
    public Course(int id, String title,String color,String image_name, Boolean active){
        this.id = id;
        this.title = title;
        this.color = color;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return id + "] " + title + " stato: " + active;
    }
}
