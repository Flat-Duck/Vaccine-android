package ly.smarthive.vaccine.models;

public class Shot {
    String number, type,takenAt,brand;

    public Shot() {}

    public Shot(String number, String type, String takenAt, String brand) {
        this.number = number;
        this.type = type;
        this.takenAt = takenAt;
        this.brand = brand;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(String takenAt) {
        this.takenAt = takenAt;
    }

    public String getBrand() {return brand;}

    public void setBrand(String brand) {this.brand = brand;}
}
