package dao;

public class User {

    private int id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String role;

    private String image_name;
    private Boolean active;

    public User(int id, String username, String role, String image_name, Boolean active){
        this.id = id;
        this.username = username;
        this.role = role;
        this.image_name = image_name;
        this.active = active;
    }

    // => Getter & Setter


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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
