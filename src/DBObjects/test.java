/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBObjects;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Nicki
 */
public class test {
    
     public static void main(String[] arg) {
         
         EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
         EntityManager emq = emf.createEntityManager();
         Query allUsersq = emq.createNativeQuery("select * from Users", Users.class);

         List<Users> allUsers = allUsersq.getResultList();
         for(Users u: allUsers){
             System.out.println("u "+u.getUserName());
         }
         
         
         
//             Users u = new Users("nicki2");
//             u.setPassword("trerer");
//             u.setUserType("manager");
//             
//             persist(u);
    
             
    
//         System.out.println("user" + u.getUserType());    
    
     }

    public static void persist(Object object) {
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
    

    
    
    
}
