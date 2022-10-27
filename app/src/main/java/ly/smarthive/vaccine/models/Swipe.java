package ly.smarthive.vaccine.models;

public class Swipe {
    String number,result,takenAt;

    public Swipe() {}

    public Swipe(String number, String result, String takenAt) {
        this.number = number;
        this.result = result;
        this.takenAt = takenAt;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(String takenAt) {
        this.takenAt = takenAt;
    }
}
