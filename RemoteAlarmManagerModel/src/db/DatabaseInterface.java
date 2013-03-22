package db;

import entities.Alarm;
import entities.User;
import exceptions.DatabaseException;
import java.util.List;

/**
 *
 * @author ivar
 */
public interface DatabaseInterface {
    
    // User CRUD methods
    public User createUser(User user) throws DatabaseException;
    public User getUser(int id) throws DatabaseException;
    public void deleteUser(User user) throws DatabaseException;
    public void deleteUser(int id) throws DatabaseException;
    public User updateUser(User user) throws DatabaseException;
    public List<User> getAllUsers();
    public List<Alarm> getAlarmsFromUser(User user) throws DatabaseException;
    public User getUser(String email) throws DatabaseException;
    public boolean checkIfEmailExists(String email) throws DatabaseException;
    public void clearAlarmsFromUser(User user) throws DatabaseException;
    
    // Alarm CRUD methods
    public Alarm createAlarm(Alarm alarm) throws DatabaseException;
    public Alarm getAlarm(int id) throws DatabaseException;
    public void deleteAlarm(Alarm alarm) throws DatabaseException;
    public void deleteAlarm(int id) throws DatabaseException;
    public Alarm updateAlarm(Alarm alarm) throws DatabaseException;
    public List<Alarm> getAllAlarms();
    public List<User> getUsersFromAlarm(Alarm alarm) throws DatabaseException;
    
    // Relationship managing methods
    public User addAlarmToUser(User user, Alarm alarm) throws DatabaseException;
    public User addAlarmToUser(int userId, int alarmId) throws DatabaseException;
    public User removeAlarmFromUser(User user, Alarm alarm) throws DatabaseException;
    public User removeAlarmFromUser(int userId, int alarmId) throws DatabaseException;
    
    // Cleanup methods
    public void cleanUpAlarms() throws DatabaseException;
    public void closeConnection();
    
    
}
