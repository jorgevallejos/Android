package be.cegeka.model;

import java.util.Calendar;

/**
 *
 * @author davidm
 */
public class Alarm {

    private long date;
    private String info;
    private int id;

    public Alarm() {
    }

    public Alarm(int id, String info, long date) {
        this.date = date;
        this.id = id;
        this.info = info;
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

    
    public String toString(){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(getDate());
    	return "id: " +  id + " info: " + info + " date: " + calendar.getTime();
    }
   
}
