package be.cegeka;

import java.util.Calendar;

/**
 *
 * @author davidm
 */
public class Alarm {

    private long date;
    private String info;
    private int id;
    private String title;
    private boolean repeated;
    private String repeatUnit;
    private int repeatQuantity;
    private long repeatEndDate;

    public Alarm() {
    }

    public Alarm(int id, String title, String info, boolean repeated, String repeat_unit, long date, int repeatQuantity, long repeatEndDate) {
        setDate(date);
        setId(id);
        setTitle(title);
        setInfo(info);
        setRepeated(repeated);
        setRepeatUnit(repeat_unit);
        setRepeatQuantity(repeatQuantity);
        setRepeatEndDate(repeatEndDate);
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

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

    public boolean isRepeated() {
        return repeated;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }

    public String getRepeatUnit() {
        return repeatUnit;
    }

    public void setRepeatUnit(String repeatUnit) {
        this.repeatUnit = repeatUnit;
    }

    public int getRepeatQuantity() {
        return repeatQuantity;
    }

    public void setRepeatQuantity(int repeatQuantity) {
        this.repeatQuantity = repeatQuantity;
    }

    public long getRepeatEndDate() {
        return repeatEndDate;
    }

    public void setRepeatEndDate(long repeatEndDate) {
        this.repeatEndDate = repeatEndDate;
    }

    public String toString() {
        return "title: " + getTitle() + " info: " + getInfo() + " repeated: " + isRepeated() + " repeatunit: " + getRepeatUnit() + " repeatQuantity: " + getRepeatQuantity() + " repeatEndDate: " + getRepeatEndDate();
    }
}
