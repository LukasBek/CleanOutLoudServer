/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package cleanoutloudserver;

import DBObjects.AnsweredQuiz;
import DBObjects.Camp;
import DBObjects.Comment;
import DBObjects.Message;
import DBObjects.Quiz;
import DBObjects.QuizAnswer;
import DBObjects.User;
import brugerautorisation.transport.rmi.Brugeradmin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

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
    public String login(String userName, String password) throws CustomErrorMessage {
        System.out.println("username: " + userName + "\n password: " + password);
        try {
            return loginWithBrugerAutMod(userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Kunne ikke logge ind via brugeraut.. modulet");
            String token = generateToken(userName);
            Query usersql = emq.createNativeQuery("SELECT * FROM User WHERE userName='" + userName + "' AND password='" + password+"';", User.class);
            List<User> allLoggedInUsers = usersql.getResultList();
            if (usersql.getResultList().size() == 1) {
                User user = allLoggedInUsers.get(0);
                user.setToken(token);
                persistMerge(user);
                return token;
            } else {
                System.out.println("Kunne ikke logge ind via CoL");
                throw new CustomErrorMessage("Kunne ikke logge ind via brugeradmin modulet eller CoL");
            }
        }
    }
    
    private String loginWithBrugerAutMod(String userName, String password) throws NotBoundException, MalformedURLException, RemoteException {
        Brugeradmin ba;
        ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        ba.hentBruger(userName, password);
        System.out.println("Login success");
        String token = generateToken(userName);
        synchronized(brugerAutModulTokens) {
            brugerAutModulTokens.add(token);
        }
        return token;
    }
    
    protected static SecureRandom random = new SecureRandom();
    synchronized String generateToken(String username) {
        long longToken = Math.abs(random.nextLong());
        String random = Long.toString(longToken, 16);
        return (username + ":" + random);
    }
    
    @Override
    public List<Camp> getCamps() {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Query campssql = emq.createNativeQuery("SELECT * FROM Camp;", Camp.class);
        List<Camp> allCamps  = campssql.getResultList();
        return allCamps;
    }
    
    @Override
    public List<Camp> getCampsSortedInWeight() {
        List<Camp> camps = getCamps();
        
        for (int i = 0; i < camps.size()-1; i++) {
            for (int k = 0; k < camps.size()-1;k++){
                if (camps.get(k).getGarbageWeight() > camps.get(k+1).getGarbageWeight()){
                    Collections.swap(camps, k, k+1);
                }
            }
        }
        Collections.reverse(camps);
        return camps;
    }
    
    @Override
    public void deleteCamp(String campName, String token) {
        try {
            if (getUserFromToken(token).getUserType().equals("admin")) {
                Camp campToDelete = getCamp(campName);
                persistDelete(campToDelete);
            }
        } catch (CustomErrorMessage ex) {
            ex.printStackTrace();
            //TODO Håndter fejlen!
        }
        
        
    }
    
    @Override
    public void addCamp(String campName, String token) throws CustomErrorMessage {
        if (getUserFromToken(token).getUserType().equals("admin")) {
        Camp newCamp = new Camp();
        newCamp.setCampName(campName);
        newCamp.setGarbageWeight(0);
        persistInsert(newCamp);
        } else {
            throw new CustomErrorMessage("Det er kun tilladt for admins at oprette nye camps");
        }
    }
    
    private Camp getCamp(String campName) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        try {
            Query campssql = emq.createNativeQuery("SELECT * FROM Camp WHERE campName='" + campName + "';", Camp.class);
            Camp camp = (Camp) campssql.getSingleResult();
            return camp;
        } catch (Exception e) {
            throw new CustomErrorMessage("Der fandtes ingen camp med navnet: " + campName);
        }
    }
    
    @Override
    public void setGarbage(String campName, Float weight,  String token) throws CustomErrorMessage {
        if (getUserFromToken(token).getUserType().equals("admin")) {
            Camp camp = getCamp(campName);
            camp.setGarbageWeight(camp.getGarbageWeight() + weight);
            persistMerge(camp);
        } else {
            throw new CustomErrorMessage("Du skal være admin for at kunne tilføje skrald til en camp");
        }
    }
    
    @Override
    public void createUser(String userName, String password, String camp, String userType, String token) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        if (userAlreadyExist(userName)) {
            throw new CustomErrorMessage("Brugernavn findes allerede!");
        }
        
        User newUser = new User();
        if (userType.equals("user")) {
            newUser.setUserType(userType);
        } else if (userType.equals("admin")) {
            User requestingUser = getUserFromToken(token);
            if (requestingUser.getUserType().equals("admin")) {
                newUser.setUserType(userType);
            } else {
                throw new CustomErrorMessage("Det er kun tilladt for admins at oprette admins");
            }
        } else {
            throw new CustomErrorMessage("userType skal sættes til enten 'user' eller 'admin'");
        }
        
        newUser.setUserName(userName);
        newUser.setPassword(password);
        Query campssql = emq.createNativeQuery("SELECT * FROM Camp WHERE campName='" + camp + "';", Camp.class);
        try{
            Camp campFromDB = (Camp) campssql.getSingleResult();
            newUser.setCamp(campFromDB);
            newUser.setToken("");
            persistInsert(newUser);
        }catch (Exception e) {
            throw new CustomErrorMessage("Navnet på campen stemmer ikke overens med nogle camps i databasen");
        }
    }
    
    @Override
    public List<Message> getWallMessages() {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Query messagesql = emq.createNativeQuery("SELECT * FROM Message;", Message.class);
        List<Message> allMessages  = messagesql.getResultList();
        
        for (Message m: allMessages) {
            String str = m.getUser().getUserName() + " fra camp " + m.getUser().getCamp().getCampName() + ": \n\r" + m.getText();
            m.setText(str.substring(0,1).toUpperCase() + str.substring(1));
            m.setUser(null);
        }
        return allMessages;
    }
    
    @Override
    public Message getMessage(int messageId) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        Query messagesql = emq.createNativeQuery("SELECT * FROM Message WHERE messageId='" + messageId + "';", Message.class);
        
        if (messagesql.getResultList().size() == 1) {
            Message msg = (Message) messagesql.getSingleResult();
            return msg;
        } else {
            throw new CustomErrorMessage("Der skete en fejl under hentning af beskeden. Kunne ikke findes i databasen");
        }
    }
    
    @Override
    public void addMessage(String message, String token) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Message msg = new Message();
        msg.setText(message);
        msg.setUser(getUserFromToken(token));
        msg.setDate(new Date());
        persistInsert(msg);
    }
    
    @Override
    public void addComment(String comment, Message message, String token) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Comment commentObject = new Comment();
        commentObject.setDate(new Date());
        commentObject.setMessageId(message);
        commentObject.setText(comment);
        commentObject.setUser(getUserFromToken(token));
        persistInsert(commentObject);
    }
    
    
    
//    private Comment getComment(int commentId, String token) throws CustomErrorMessage {
//        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
//        EntityManager emq = emf.createEntityManager();
//        
//        Query commentsql = emq.createNativeQuery("SELECT * FROM Comment WHERE commentsid='" + commentId + "';", Comment.class);
//        if (commentsql.getResultList().size() == 1){
//            Comment comment = (Comment) commentsql.getSingleResult();
//            return comment;
//        } else {
//            throw new CustomErrorMessage("Kommentaren kunne ikke findes");
//        }
//    }
    
    @Override
    public List<Comment> getCommentsForMessage(int messageId) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Query commentsql = emq.createNativeQuery("SELECT * FROM Comment WHERE messageId='" + messageId + "';", Comment.class);
        List<Comment> comments = commentsql.getResultList();
        
        for (Comment c: comments) {
            String str = c.getUser().getUserName() + " fra camp " + c.getUser().getCamp().getCampName() + ": \n\r" + c.getText();
            
            c.setText(str.substring(0,1).toUpperCase() + str.substring(1));
            c.setUser(null);
        }
        return comments;
    }
    
    private User getUserFromToken(String token) throws CustomErrorMessage {
        if (!token.equals("")) {
            synchronized(brugerAutModulTokens) {
                if (brugerAutModulTokens.contains(token)) {
                    User brugerAutUser = new User();
                    brugerAutUser.setToken(token);
                    brugerAutUser.setUserName(token.substring(0, token.indexOf(':')));
                    brugerAutUser.setUserType("admin");
                    return brugerAutUser;
                }
            }
            EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
            EntityManager emq = emf.createEntityManager();
            
            Query userdb = emq.createNativeQuery("SELECT * FROM User WHERE token='" + token + "';", User.class);
            if (!(userdb.getResultList().size() == 1)) {
                throw new CustomErrorMessage("Der benyttes et ugyldigt token. Log ind igen og få det rigtige token");
            }
            User user = (User) userdb.getSingleResult();
            return user;
        } else {
            throw new CustomErrorMessage("Der er ikke logget ind. Token er tomt");
        }
    }
    
    @Override
    public void setUser(User user, String token) throws CustomErrorMessage {
        
        User requestingUser = getUserFromToken(token);
        
        if (requestingUser.getUserType().equals("admin")) {
            persistMerge(user);
        } else {
            throw new CustomErrorMessage("Det er kun admins der kan ændre i brugere");
        }
    }
    
    @Override
    public User getUser(String userName, String token) throws CustomErrorMessage {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        User reqUser = getUserFromToken(token);
        if (reqUser.getUserName().equals(userName) || reqUser.getUserType().equals("admin")) {
            Query usersql = emq.createNativeQuery("SELECT * FROM User WHERE userName='" + userName + "';", User.class);
            User user = (User) usersql.getSingleResult();
            System.out.println("NICKI TEST: " + user.getCamp().getCampName());
            return user;
        } else {
            throw new CustomErrorMessage("Det er kun tilladt for admins at hente brugerprofiler");
        }
    }
    
    private boolean userAlreadyExist(String userName) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager emq = emf.createEntityManager();
        
        Query usersql = emq.createNativeQuery("SELECT * FROM User WHERE userName='" + userName + "';", User.class);
        
        if (usersql.getResultList().size() >= 1) {
            return true;
        } else {
            return false;
        }
    }
    
    
    //---------------------------------------------------------------------------------------
    //database persist
    
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
    
    public synchronized void persistDelete(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.remove(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
    }
    
    
    
//    @Override
//    public Quiz getQuiz(String quizName) {
//        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
//        EntityManager emq = emf.createEntityManager();
//        
//        Query quizsql = emq.createNativeQuery("SELECT * FROM Quiz WHERE name='" + quizName + "';", Quiz.class);
//        Quiz quiz = (Quiz) quizsql.getSingleResult();
//        
//        return quiz;
//    }
//    
//    @Override
//    public void addQuiz(String quizName, String textQuestion, String token) throws CustomErrorMessage {
//        if (getUserFromToken(token).getUserType().equals("admin")) {
//            try {
//                Quiz newQuiz = new Quiz();
//                newQuiz.setName(quizName);
//                newQuiz.setTextQuestion(textQuestion);
//                persistInsert(newQuiz);
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new CustomErrorMessage("Der skete en fejl ved indsættelse af den nye quiz. Prøv et andet navn.");
//            }
//        } else {
//            throw new CustomErrorMessage("Det er kun muligt for admins at oprette nye spørgsmål");
//        }
//    }
//    
//    @Override
//    public void addUsersOfAnsweredQuizzes(String quizName, String token) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    
//    @Override
//    public Quiz addQuizAnswers(String quizName) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    
//     @Override
//    public List<QuizAnswer> getQuizAnswers(String quizName) {
//        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
//        EntityManager emq = emf.createEntityManager();
//        
//        Query quizsql = emq.createNativeQuery("SELECT * FROM quizAnswer WHERE name='" + quizName + "';", QuizAnswer.class);
//        List<QuizAnswer> quizAnswers = (List<QuizAnswer>) quizsql.getResultList();
//        return quizAnswers;
//    }
//    
//    @Override
//    public List<AnsweredQuiz> getUsersOfAnsweredQuizzes(String quizName) {
//        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
//        EntityManager emq = emf.createEntityManager();
//        
//        Query quizAnswerssql = emq.createNativeQuery("SELECT * FROM AnsweredQuiz WHERE name='" + quizName + "';", AnsweredQuiz.class);
//        List<AnsweredQuiz> ansQuizzes = quizAnswerssql.getResultList();
//        return ansQuizzes;
//    }
    
//     @Override
//    public List<Quiz> getQuizzes() {
//        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
//        EntityManager emq = emf.createEntityManager();
//        
//        Query quizsql = emq.createNativeQuery("SELECT * FROM Quiz;", Quiz.class);
//        List<Quiz> quizzes = quizsql.getResultList();
//        
//        return quizzes;
//    }
    
//    @Override
//    public void deleteUser(String userName, String token) throws CustomErrorMessage {
//        User reqUser = getUserFromToken(token);
//        if (reqUser.getUserType().equals("admin") || reqUser.getUserName().equals(userName)){
//            EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
//            EntityManager emq = emf.createEntityManager();
//            
//            try {
//                Query messagesql = emq.createNativeQuery("SELECT * FROM Message WHERE user='" + userName + "';", Message.class);
//                List<Message> messages = messagesql.getResultList();
//                for (Message m: messages) {
//                    emq.createNativeQuery("DELETE FROM Comment WHERE messageId='" + m.getMessageId() + "';");
//                }
//                emq.createNativeQuery("DELETE FROM Message WHERE user='" + userName + "';");
//                emq.createNativeQuery("DELETE FROM User WHERE userName='" + userName + "';");
//            } catch (Exception e) {
//                throw new CustomErrorMessage("Kunne ikke slette den aktuelle bruger");
//            }
//        } else {
//            throw new CustomErrorMessage("Det er kun tiladt at slette en bruger hvis du er admin eller det er din egen bruger");
//        }
//    }
    
    
}
