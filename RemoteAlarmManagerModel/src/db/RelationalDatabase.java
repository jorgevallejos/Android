package db;

import entities.Alarm;
import entities.User;
import entities.UserAlarm;
import exceptions.DatabaseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author ivar
 */
public class RelationalDatabase implements DatabaseInterface {

    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("RemoteAlarmManagerModelPU");
    private EntityManager em = factory.createEntityManager();
    private static RelationalDatabase instance = new RelationalDatabase();

    public static RelationalDatabase getInstance() {
        return instance;
    }

    public RelationalDatabase() {
    }

    @Override
    public User createUser(User user) throws DatabaseException {
        if (user == null) {
            throw new DatabaseException("User can't be null.");
        }
        beginTransaction();
        em.persist(user);
        commitTransaction();
        return user;
    }

    @Override
    public User getUser(int id) throws DatabaseException {
        return em.find(User.class, id);
    }

    @Override
    public void deleteUser(User user) throws DatabaseException {
        if (user == null) {
            throw new DatabaseException("User can't be null.");
        }
        // Delete useralarms associated with user
        clearAlarmsFromUser(user);
        // Delete user
        beginTransaction();
        Query q2 = em.createQuery("DELETE FROM User u WHERE u.userid = ?1");
        q2.setParameter(1, user.getUserid());
        q2.executeUpdate();
        commitTransaction();
    }

    @Override
    public void deleteUser(int id) throws DatabaseException {
        User user = getUser(id);
        deleteUser(user);
    }

    @Override
    public User updateUser(User user) throws DatabaseException {
        if (user == null) {
            throw new DatabaseException("User can't be null.");
        }
        beginTransaction();
        em.merge(user);
        commitTransaction();
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        Query q = em.createQuery("SELECT u from User u");
        return new ArrayList<>(q.getResultList());
    }

    @Override
    public List<Alarm> getAlarmsFromUser(User user) throws DatabaseException {
        if (user == null) {
            throw new DatabaseException("User can't be null.");
        }
        Query q = em.createQuery("SELECT ua.alarmid FROM UserAlarm ua WHERE ua.userid = ?1");
        q.setParameter(1, user);
        ArrayList<Alarm> alarms = new ArrayList<>(q.getResultList());
        commitTransaction();
        return alarms;
    }

    @Override
    public Alarm createAlarm(Alarm alarm) throws DatabaseException {
        if (alarm == null) {
            throw new DatabaseException("Alarm can't be null.");
        }
        beginTransaction();
        em.persist(alarm);
        commitTransaction();
        return alarm;
    }

    @Override
    public Alarm getAlarm(int id) throws DatabaseException {
        return em.find(Alarm.class, id);
    }

    @Override
    public void deleteAlarm(Alarm alarm) throws DatabaseException {
        if (alarm == null) {
            throw new DatabaseException("Alarm can't be null.");
        }
        beginTransaction();
        Query q = em.createQuery("DELETE FROM UserAlarm ua WHERE ua.alarmid = ?1");
        q.setParameter(1, alarm);
        q.executeUpdate();
        em.flush();
        em.remove(alarm);
        commitTransaction();
    }

    @Override
    public void deleteAlarm(int id) throws DatabaseException {
        Alarm alarm = getAlarm(id);
        deleteAlarm(alarm);
    }

    @Override
    public Alarm updateAlarm(Alarm alarm) throws DatabaseException {
        if (alarm == null) {
            throw new DatabaseException("Alarm can't be null.");
        }
        beginTransaction();
        em.merge(alarm);
        commitTransaction();
        return alarm;
    }

    @Override
    public List<Alarm> getAllAlarms() {
        Query q = em.createQuery("SELECT a FROM Alarm a");
        return new ArrayList<>(q.getResultList());
    }

    @Override
    public List<User> getUsersFromAlarm(Alarm alarm) throws DatabaseException {
        if (alarm == null) {
            throw new DatabaseException("Alarm can't be null.");
        }
        Query q = em.createQuery("SELECT ua.userid FROM UserAlarm ua WHERE ua.alarmid = ?1");
        q.setParameter(1, alarm);
        return new ArrayList<>(q.getResultList());
    }

    @Override
    public User addAlarmToUser(User user, Alarm alarm) throws DatabaseException {
        if (alarm == null) {
            throw new DatabaseException("Alarm can't be null.");
        }
        if (user == null) {
            throw new DatabaseException("User can't be null.");
        }
        if (checkIfUserAlarmExists(user, alarm)) {
            throw new DatabaseException("The user already has this alarm.");
        }
        if (user.getUserid() == null || alarm.getAlarmid() == null) {
            throw new DatabaseException("User or Alarm is not persisted yet.");
        }
        UserAlarm userAlarm = new UserAlarm(user, alarm);
        user.getUserAlarmList().add(userAlarm);
        updateUser(user);
        return user;
    }

    private boolean checkIfUserAlarmExists(User user, Alarm alarm) {
        beginTransaction();
        Query q = em.createQuery("SELECT ua FROM UserAlarm ua WHERE ua.userid=?1 AND ua.alarmid=?2");
        q.setParameter(1, user);
        q.setParameter(2, alarm);
        List<UserAlarm> userAlarms = q.getResultList();
        commitTransaction();
        return !userAlarms.isEmpty();
    }

    @Override
    public User removeAlarmFromUser(User user, Alarm alarm) throws DatabaseException {
        if (alarm == null) {
            throw new DatabaseException("Alarm can't be null.");
        }
        if (user == null) {
            throw new DatabaseException("User can't be null.");
        }
        beginTransaction();
        Query q = em.createQuery("DELETE FROM UserAlarm ua WHERE ua.alarmid = ?1 AND ua.userid = ?2", UserAlarm.class);
        q.setParameter(1, alarm);
        q.setParameter(2, user);
        q.executeUpdate();
        commitTransaction();
        return user;
    }

    @Override
    public User removeAlarmFromUser(int userId, int alarmId) throws DatabaseException {
        User user = getUser(userId);
        Alarm alarm = getAlarm(alarmId);
        return removeAlarmFromUser(user, alarm);
    }

    @Override
    public void cleanUpAlarms() throws DatabaseException {
        List<Alarm> alarms = getAllAlarms();
        for (Alarm a : alarms) {
            Calendar alarmCalendar = getCalendarFromMillis(a.getDateInMillis());
            if (alarmCalendar.before(Calendar.getInstance())) {
                deleteAlarm(a);
            }
        }
    }

    private Calendar getCalendarFromMillis(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal;
    }

    @Override
    public User addAlarmToUser(int userId, int alarmId) throws DatabaseException {
        User user = getUser(userId);
        Alarm alarm = getAlarm(alarmId);
        return addAlarmToUser(user, alarm);
    }

    @Override
    public void closeConnection() {
        em.close();
        factory.close();
    }

    @Override
    public User getUser(String email) throws DatabaseException {
        if (email == null) {
            throw new DatabaseException("Email can't be null.");
        }
        Query q = em.createQuery("SELECT u FROM User u WHERE u.email = ?1", User.class);
        q.setParameter(1, email);
        User u = (User) q.getSingleResult();

        return u;
    }

    @Override
    public boolean checkIfEmailExists(String email) throws DatabaseException {
        beginTransaction();
        Query q = em.createQuery("SELECT u FROM User u WHERE lower(u.email) = ?1", User.class);
        q.setParameter(1, email.toLowerCase());
        if (q.getResultList().size() > 0) {
            commitTransaction();
            return true;
        } else {
            commitTransaction();
            return false;
        }
    }

    public void clearAlarmsFromUser(User user) {
        // Delete all UserAlarms associated with this user.
        beginTransaction();
        Query q = em.createQuery("DELETE FROM UserAlarm ua WHERE ua.userid = ?1");
        q.setParameter(1, user);
        q.executeUpdate();
        commitTransaction();
    }

    private void beginTransaction() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }
}
