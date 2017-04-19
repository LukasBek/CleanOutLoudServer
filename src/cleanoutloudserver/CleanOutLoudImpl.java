/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package cleanoutloudserver;

import DBObjects.AnsweredQuizzes;
import DBObjects.Messages;
import DBObjects.Quiz;
import DBObjects.QuizAnswers;
import DBObjects.Users;
import Objects.Camp;
import Objects.User;
import brugerautorisation.transport.rmi.Brugeradmin;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.rmi.Naming;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Nicki
 */

@WebService(endpointInterface = "cleanoutloudserver.ICleanOutLoud")
public class CleanOutLoudImpl implements ICleanOutLoud{
    
    EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
    EntityManager emq = emf.createEntityManager();
    
    
    
    @Override
    public String login(String userName, String password) throws Exception, loginError {
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        try {
            ba.hentBruger(userName, password);
            System.out.println("Login success");
            String token = generateToken(userName);
            return token;
        } catch (Exception e) {
            System.out.println("Kunne ikke logge ind via brugeraut.. modulet");
            String token = generateToken(userName);
            Query usersql = emq.createNativeQuery("SELECT * FROM Users WHERE userName='" + userName + "' AND password='" + password+"';", Users.class);
            List<Users> allLoggedInUsers = usersql.getResultList();
            if (usersql.getResultList().size() == 1) {
                Users user = allLoggedInUsers.get(0);
                user.setToken(token);
                persistMerge(user);
                return token;
            } else {
                System.out.println("Kunne ikke ligge ind via CoL");
                throw new loginError(e);
            }
        }
    }
    
    protected static SecureRandom random = new SecureRandom();
    
    synchronized String generateToken( String username ) {
        long longToken = Math.abs(random.nextLong());
        String random = Long.toString( longToken, 16 );
        return ( username + ":" + random );
    }
    
    @Override
    public QuizAnswers getQuizAnswers(String quizName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public AnsweredQuizzes getUsersOfAnsweredQuizzes(String quizName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    private Users getCorrectUser(String userName, String password) {
        Users user;
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ArrayList<String> getCamps() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String loginWithBrugerAutMod(String userName, String password) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void createUser(String userName, String password, String camp, String userType, String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void editUser(Users user, String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<Messages> getWallMessages() {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Query usersql = emq.createNativeQuery("SELECT * FROM Messages;", Messages.class);
        List<Messages> allMessages  = usersql.getResultList();
        return allMessages;
    }
    
    @Override
    public void addMessage(String message, String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void addComment(String comment, Messages message, String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ArrayList<Quiz> getQuizzes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public synchronized void persistInsert(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
        
        public synchronized void persistMerge(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
    }
    
}
