/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package cleanoutloudserver;

import DBObjects.AnsweredQuizzes;
import DBObjects.Camp;
import DBObjects.Comments;
import DBObjects.Messages;
import DBObjects.Quiz;
import DBObjects.QuizAnswers;
import DBObjects.Users;
import Objects.User;
import brugerautorisation.transport.rmi.Brugeradmin;

import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    ArrayList<String> brugerAutModulTokens = new ArrayList<>();
    
    EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
    EntityManager emq = emf.createEntityManager();
    
    
    
    @Override
    public String login(String userName, String password) throws loginError {
        System.out.println("username: " + userName + "\n password: " + password);
        
        try {
            String token = loginWithBrugerAutMod(userName, password);
            brugerAutModulTokens.add(token);
            return token;
        } catch (NotBoundException | MalformedURLException  | RemoteException e ) {
            e.printStackTrace();
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
                throw new loginError("Kunne ikke logge ind via brugeradmin modulet eller CoL");
            }
        }
    }

    public String loginWithBrugerAutMod(String userName, String password) throws NotBoundException, MalformedURLException, RemoteException {
        Brugeradmin ba;
        ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        ba.hentBruger(userName, password);
        System.out.println("Login success");
        String token = generateToken(userName);
        brugerAutModulTokens.add(token);
        return token;
    }
    
    protected static SecureRandom random = new SecureRandom();
    
    synchronized String generateToken( String username ) {
        long longToken = Math.abs(random.nextLong());
        String random = Long.toString( longToken, 16 );
        return ( username + ":" + random );
    }
    
    @Override
    public List<QuizAnswers> getQuizAnswers(String quizName) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Query quizsql = emq.createNativeQuery("SELECT * FROM quizAnswers WHERE name='" + quizName + "';", QuizAnswers.class);
        List<QuizAnswers> quizAnswers = (List<QuizAnswers>) quizsql.getResultList();
        return quizAnswers;
    }
    
    @Override
    public List<AnsweredQuizzes> getUsersOfAnsweredQuizzes(String quizName) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Query quizAnswerssql = emq.createNativeQuery("SELECT * FROM AnsweredQuizzes WHERE name='" + quizName + "';", AnsweredQuizzes.class);
        List<AnsweredQuizzes> ansQuizzes = quizAnswerssql.getResultList();
        return ansQuizzes;         
    }
    
    @Override
    public List<Camp> getCamps() {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Query campssql = emq.createNativeQuery("SELECT * FROM Camps;", Camp.class);
        List<Camp> allCamps  = campssql.getResultList();
        return allCamps;
        
    }
    
    
    
    @Override
    public void createUser(String userName, String password, String camp, String userType, String token) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        if (userAlreadyExcist(userName)) {
            throw new CustomErrorMessage("Bruger findes allerede!");
        }
        
        Users newUser = new Users();
        if (userType.equals("user")) {
            newUser.setUserType(userType);
        } else if (userType.equals("admin") || userType.equals("manager")) {
            Users requestingUser = getUserFromToken(token);
            if (requestingUser.getUserType().equals("admin")) {
                newUser.setUserType(userType);
            } else {
                throw new CustomErrorMessage("Det er kun tilladt for admins at oprette managers og admins");
            }
        } else {
            throw new CustomErrorMessage("userType skal sættes til enten 'user', 'manager' eller 'admin'");
        }
        
        newUser.setUserName(userName);
        newUser.setPassword(password);
        Query campssql = emq.createNativeQuery("SELECT * FROM Camps WHERE campName='" + camp + "';", Camp.class);
        Camp campFromDB = (Camp) campssql.getSingleResult();
        newUser.setCamp(campFromDB);
        newUser.setToken("");
        persistInsert(newUser);
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
    public void addMessage(String message, String token) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Messages msg = new Messages();
        msg.setText(message);
        msg.setUser(getUserFromToken(token));
        msg.setDate(new Date());
        persistInsert(msg);
    }
    
    @Override
    public void addComment(String comment, Messages message, String token) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Comments commentObject = new Comments();
        commentObject.setDate(new Date());
        commentObject.setMessageId(message);
        commentObject.setText(comment);
        commentObject.setUser(getUserFromToken(token));
        persistInsert(commentObject);
    }
    
    @Override
    public List<Quiz> getQuizzes() {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Query quizsql = emq.createNativeQuery("SELECT * FROM Quiz;", Quiz.class);
        List<Quiz> quizzes = quizsql.getResultList();
        
        return quizzes;
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
    
    private Users getUserFromToken(String token) throws CustomErrorMessage {
        if (!token.equals("")) {
            if (brugerAutModulTokens.contains(token)) {
                Users brugerAutUser = new Users();
                brugerAutUser.setToken(token);
                brugerAutUser.setUserName(token.substring(0, token.indexOf(':')));
                brugerAutUser.setUserType("admin");
                return brugerAutUser;
            }
            EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
            EntityManager emq = emf.createEntityManager();
            
            Query userdb = emq.createNativeQuery("SELECT * FROM Users WHERE token='" + token + "';", Users.class);
            if (!(userdb.getResultList().size() == 1)) {
                throw new CustomErrorMessage("Brugeren kunne ikke findes via token");
            }
            Users user = (Users) userdb.getSingleResult();
            return user;
        } else {
            throw new CustomErrorMessage("Der er ikke logget ind. Token findes ikke...");
        }
    }
    
    @Override
    public void setUser(Users user, String token) throws CustomErrorMessage {
        
        Users requestingUser = getUserFromToken(token);
        
        if (requestingUser.getUserType().equals("admin")) {
            persistMerge(user);
        } else {
            throw new CustomErrorMessage("Det er kun admins der kan ændre i brugere");
        }
    }
    
    @Override
    public Users getUser(String userName, String token) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        if (getUserFromToken(token).getUserType().equals("admin")) {
            Query usersql = emq.createNativeQuery("SELECT * FROM Users WHERE userName='" + userName + "';", Users.class);
            Users user = (Users) usersql.getSingleResult();
            return user;
        } else {
            throw new CustomErrorMessage("Det er kun tilladt for admins at hente brugerprofiler");
        }
    }
    
    private boolean userAlreadyExcist(String userName) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Query usersql = emq.createNativeQuery("SELECT * FROM Users WHERE userName='" + userName + "';", Users.class);
        
        if (usersql.getResultList().size() >= 1) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public Messages getMessage(int messageId) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        Query messagesql = emq.createNativeQuery("SELECT * FROM Messages WHERE messageId='" + messageId + "';", Messages.class);
        
        if (messagesql.getResultList().size() == 1) {
            Messages msg = (Messages) messagesql.getSingleResult();
            return msg;
        } else {
            throw new CustomErrorMessage("Der skete en fejl under hentning af beskeden");
        }
    }

    @Override
    public void addCamp(String campName, String token) {
        Camp newCamp = new Camp();
        newCamp.setCampName(campName);
        newCamp.setGarbageWeight(0);
        persistInsert(newCamp);
    }

    @Override
    public Quiz getQuiz(String quizName) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Query quizsql = emq.createNativeQuery("SELECT * FROM Quiz WHERE name='" + quizName + "';", Quiz.class);
        Quiz quiz = (Quiz) quizsql.getSingleResult();
        
        return quiz;
    }

    @Override
    public void addQuiz(String quizName, String textQuestion, String token) throws CustomErrorMessage {
        if (getUserFromToken(token).getUserType().equals("admin")) {
           try {
               Quiz newQuiz = new Quiz();
            newQuiz.setName(quizName);
            newQuiz.setTextQuestion(textQuestion);
            persistInsert(newQuiz);
           } catch (Exception e) {
               e.printStackTrace();
               throw new CustomErrorMessage("Der skete en fejl ved indsættelse af den nye quiz. Prøv et andet navn.");
           }
        } else {
            throw new CustomErrorMessage("Det er kun muligt for admins at oprette nye spørgsmål");
        }
    }

    @Override
    public void addUsersOfAnsweredQuizzes(String quizName, String token) {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Quiz addQuizAnswers(String quizName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
}
