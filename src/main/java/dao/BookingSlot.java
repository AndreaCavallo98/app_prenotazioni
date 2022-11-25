package dao;

public class BookingSlot {

    private int from;
    private int to;
    private boolean avaliable;

    public BookingSlot(int from, int to, boolean avaliable){
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
