package dao;

public class Review {

    private int id;
    private String user_name;
    private String user_surname;

    private String user_image;
    private int rate;
    private String title;
    private String text;
    private String creation_date;

    public Review(int id, String user_name, String user_surname, String user_image, int rate, String title, String text, String creation_date) {
        this.id = id;
        this.user_name = user_name;
        this.user_surname = user_surname;
        this.user_image = user_image;
        this.rate = rate;
        this.title = title;
        this.text = text;
        this.creation_date = creation_date;
    }


}
