/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transferobjects;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author ivarv
 */
public class AlarmTO {

    private Integer id = -1;
    private String title;
    private String info;
    private boolean repeated;
    private String repeatunit = "N/A";
    private int repeatQuantity = 0;
    private String eventDateTimeString = formatCalendar(Calendar.getInstance());
    private String endRepeatDateTimeString = formatCalendar(Calendar.getInstance());

    public AlarmTO(Integer id, String title, String info, boolean repeated, String repeatunit, Integer repeatQuantity, BigInteger endDate, long date) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.repeated = repeated;
        if (repeatunit != null) {
            this.repeatunit = repeatunit;
        }
        if (repeatQuantity != null) {
            this.repeatQuantity = repeatQuantity;
        }
        if (endDate != null) {
            Calendar endDateCal = calendarFromBigInt(endDate);
            this.endRepeatDateTimeString = formatCalendar(endDateCal);
        }
        Calendar dateCal = calendarFromLong(date);
        this.eventDateTimeString = formatCalendar(dateCal);

    }

    private String formatCalendar(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    private long stringToMillis(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        cal.setTime(sdf.parse(date));
        return cal.getTimeInMillis();
    }

    public AlarmTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isRepeated() {
        return repeated;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }

    public String getRepeatunit() {
        return repeatunit;
    }

    public void setRepeatunit(String repeatunit) {
        this.repeatunit = repeatunit;
    }

    public int getRepeatQuantity() {
        return repeatQuantity;
    }

    public void setRepeatQuantity(int repeatQuantity) {
        this.repeatQuantity = repeatQuantity;
    }

    public String getEventDateTimeString() {
        return eventDateTimeString;
    }

    public void setEventDateTimeString(String eventDateTimeString) {
        this.eventDateTimeString = eventDateTimeString;
    }

    public String getEndRepeatDateTimeString() {
        return endRepeatDateTimeString;
    }

    public void setEndRepeatDateTimeString(String endRepeatDateTimeString) {
        this.endRepeatDateTimeString = endRepeatDateTimeString;
    }

    private Calendar calendarFromLong(long date) {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(date);
        return dateCalendar;
    }

    private Calendar calendarFromBigInt(BigInteger endDate) {
        Calendar repeatEndCalendar = Calendar.getInstance();
        long endDateMillis = endDate.longValue();
        repeatEndCalendar.setTimeInMillis(endDateMillis);
        return repeatEndCalendar;
    }

    public long getEventDateInMillis() throws ParseException {
        return stringToMillis(getEventDateTimeString());
    }

    public BigInteger getEndDateInMillis() throws ParseException {
        return BigInteger.valueOf(stringToMillis(getEndRepeatDateTimeString()));
    }
}
