package dao;

public class BookingSlot {

    private String date;
    private int from;
    private int to;
    private boolean avaliable;

    public BookingSlot(String date, int from, int to, boolean avaliable){
        this.date = date;
        this.from = from;
        this.to = to;
        this.avaliable = avaliable;
    }

    public int getFrom() {
        return from;
    }

    public void setAvaliable(boolean avaliable) {
        this.avaliable = avaliable;
    }
}
