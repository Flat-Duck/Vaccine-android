package ly.smarthive.vaccine.models;

public class Request {

    int id;
    String  from, date,phone;
    int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Request() {
    }

    public Request(int id, String from, String date) {
        this.id = id;
        this.from = from;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }
}
