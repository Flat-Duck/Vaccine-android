package ly.smarthive.vaccine.models;

public class Donor {
    int id;
    String blood,address;
    boolean requested;

    public Donor() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBlood() { return blood; }

    public void setBlood(String blood) { this.blood = blood; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }
}
