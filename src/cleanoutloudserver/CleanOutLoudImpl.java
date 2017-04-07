/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cleanoutloudserver;

import DBObjects.AnsweredQuizzes;
import DBObjects.Quiz;
import DBObjects.QuizAnswers;
import DBObjects.Users;
import Objects.Camp;
import Objects.User;
import brugerautorisation.transport.soap.Brugeradmin;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.rmi.Naming;
import java.security.SecureRandom;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Nicki
 */

@WebService(endpointInterface = "cleanoutloudserver.ICleanOutLoud")
public class CleanOutLoudImpl implements ICleanOutLoud{

    @Override
    public Camp getCamp(String campName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String login(String userName, String password) throws Exception {
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        try {
            ba.hentBruger(userName, password);
            
            System.out.println("Login success");
            String token = generateToken(userName);
            return token;
            
            
        } catch (Exception e) {
            
            try {
                
                
                
                
                
                String token = generateToken(userName);
                return token;
                
            } catch (Exception g) {
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
    public Quiz getQuiz() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public QuizAnswers getQuizAnswers(String quizName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnsweredQuizzes getUsersOfAnsweredQuizzes(String quizName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void persist(Object object) {
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
    
    private Users getCorrectUser(String userName, String password) {
        Users user;
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    
    
}
