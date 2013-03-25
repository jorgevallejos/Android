/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.cegeka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.sql.DataSource;

/**
 *
 * @author davidm
 */
@WebService(serviceName = "AlarmWebService")
@Stateless()
public class AlarmWebService {

    @Resource(name = "AlarmDatabase")
    private DataSource alarmDatabase;
    
  

    /**
     * Web service operation
     */
    @WebMethod(operationName = "createUser")
    public User createUser(@WebParam(name = "naam") String naam, @WebParam(name = "achternaam") String achternaam, @WebParam(name = "email") String email, @WebParam(name = "pass") String pass, @WebParam(name = "salt") String salt) {
        User user = null;

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            PreparedStatement st = con.prepareStatement("insert into Users (naam,achternaam,email, paswoord, salt) values(?,?,?,?,?)");
            st.setString(1, naam);
            st.setString(2, achternaam);
            st.setString(3, email);
            st.setString(4, pass);
            st.setString(5, salt);
            st.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    
    

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAlarmsFromUser")
    public ArrayList<Alarm> getAlarmsFromUser(@WebParam(name = "emailadres") String emailadres, @WebParam(name = "paswoord") String passwoord) {
        if(getUser(emailadres, passwoord)==null){
            return null;
        }
        
        String result = "";
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            
            PreparedStatement st = con.prepareStatement("SELECT * from users WHERE email = ?");
            st.setString(1, emailadres);
            
            // execute select SQL stetement
            ResultSet rs = st.executeQuery();
            int userid;
            while (rs.next()) {
                userid = (int) rs.getInt("userid");
                PreparedStatement st2 = con.prepareStatement("SELECT alarmid, title, info, repeated, repeat_unit, date_in_millis, repeatquantity, repeat_enddate FROM alarm WHERE alarmid IN (SELECT alarmid from user_alarm WHERE userid = ?)");
                st2.setInt(1, userid);
                ResultSet rs2 = st2.executeQuery();
                
                while (rs2.next()) {
                    Alarm a = new Alarm(rs2.getInt("alarmid"), rs2.getString("title"), rs2.getString("info"), rs2.getBoolean("repeated"), rs2.getString("repeat_unit"), rs2.getLong("date_in_millis"), rs2.getInt("repeatquantity"), rs2.getLong("repeat_enddate"));
                    alarms.add(a);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Alarm a : alarms){
            System.out.println(a.toString());
        }
        return alarms;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addAlarm")
    public Alarm addAlarm(@WebParam(name = "titel") String titel,@WebParam(name = "info") String info,@WebParam(name = "repeated") boolean repeated, @WebParam(name = "repeat_unit") String repeat_unit, @WebParam(name = "datum") long datum, @WebParam(name = "repeatquantity") int repeatquantity, @WebParam(name = "repeat_enddate") long repeat_enddate) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            PreparedStatement st = con.prepareStatement("insert into alarm (title, info, repeated, repeat_unit, date_in_millis, repeatquantity, repeat_enddate) values(?,?,?,?,?,?,?)");
            st.setString(1, titel);
            st.setString(2, info);
            st.setBoolean(3, repeated);
            st.setString(4, repeat_unit);
            st.setLong(5, datum);
            st.setInt(6, repeatquantity);
            st.setLong(7, repeat_enddate);
            st.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
     @WebMethod(operationName = "addAlarmByDate")
    public Alarm addAlarmByDate(@WebParam(name = "titel") String titel,@WebParam(name = "info") String info,@WebParam(name = "repeated") boolean repeated, @WebParam(name = "repeat_unit") String repeat_unit, @WebParam(name = "datum") Calendar datum, @WebParam(name = "repeatquantity") int repeatquantity, @WebParam(name = "repeat_enddate") long repeat_enddate) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            PreparedStatement st = con.prepareStatement("insert into alarm (title, info, repeated, repeat_unit, date_in_millis, repeatquantity, repeat_enddate) values(?,?,?,?,?,?,?)");
            st.setString(1, titel);
            st.setString(2, info);
            st.setBoolean(3, repeated);
            st.setString(4, repeat_unit);
            st.setLong(5, datum.getTimeInMillis());
            st.setInt(6, repeatquantity);
            st.setLong(7, repeat_enddate);
            st.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getUser")
    public User getUser(@WebParam(name = "emailadres") String emailadres, @WebParam(name = "paswoord") String paswoord) { 
       User u = null;
       try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            PreparedStatement st = con.prepareStatement("SELECT * from users WHERE email = ? and paswoord = ?");
            st.setString(1, emailadres);
            st.setString(2, paswoord);
            // execute select SQL stetement
            ResultSet rs = st.executeQuery();
            int userid;
            while (rs.next()) {
                u = new User(rs.getInt("userid"), rs.getString("naam"), rs.getString("achternaam"), rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }

    
}
