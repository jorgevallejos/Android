/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import domain.AlarmOrganizer;
import entities.Alarm;
import entities.User;
import exceptions.DatabaseException;
import java.math.BigInteger;
import java.text.ParseException;
import transferobjects.AlarmTO;
import transferobjects.UserTO;

/**
 *
 * @author ivarv
 */
public class TransferObjectConverter {
    
    public static AlarmTO getAlarmTO(Alarm alarm){
        AlarmTO alarmTO = new AlarmTO(alarm.getAlarmid(), alarm.getTitle(), alarm.getInfo(), 
                alarm.getRepeated(), alarm.getRepeatUnit(), alarm.getRepeatquantity(), alarm.getRepeatEnddate(), alarm.getDateInMillis());
        return alarmTO;
    }    

    public static Alarm getAlarm(AlarmTO alarmTO) throws ParseException{
        Alarm alarm = new Alarm(alarmTO.getId(), alarmTO.getTitle(), alarmTO.getInfo(), alarmTO.isRepeated(), 
                alarmTO.getRepeatunit(), alarmTO.getEventDateInMillis(), alarmTO.getRepeatQuantity(), alarmTO.getEndDateInMillis());
        return alarm;     
    }
    
    public static UserTO getUserTO(User user){
        UserTO userTO = new UserTO(user);
        return userTO;
    }
    
    public static User getUser(UserTO userTO) throws DatabaseException{
        AlarmOrganizer organizer = new AlarmOrganizer();
        User user = organizer.getUser(userTO.getId());
        return user;
    }
}
