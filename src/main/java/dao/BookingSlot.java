package dao;

public class BookingSlot {

    private String date;
    private int from;
    private int to;
    private String status;

    public BookingSlot(String date, int from, int to, String status){
        this.date = date;
        this.from = from;
        this.to = to;
        this.status = status;
    }

    public int getFrom() {
        return from;
    }

    public String getStatus() {
        return status;
    }

    public void setAvaliable(String status) {
        this.status = status;
    }
}
